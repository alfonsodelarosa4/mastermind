package mastermind.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import mastermind.backend.model.GameSession;
import mastermind.backend.service.GameEventService;
import mastermind.backend.service.GameSessionService;
import mastermind.backend.service.WebSocketMessagingService;
import mastermind.backend.utils.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private GameEventService gameEventService;
    @Autowired
    private final WebSocketMessagingService webSocketMessagingService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    public WebSocketController(
           GameSessionService gameSessionService,
           WebSocketMessagingService webSocketMessagingService) {
        this.gameSessionService = gameSessionService;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    // receives guess from client
    @MessageMapping("/guess")
    public void handleGuess(@Payload Map<String, String> receivedMessage) {
        try{
            logger.info("WS /guess: {}", receivedMessage);
            // get game session id and guess
            String gameSessionId = receivedMessage.get("gameSessionId");
            String guess = receivedMessage.get("guess");
            // get game session
            Optional<GameSession> retrievedGameSession = gameSessionService.findById(gameSessionId);
            if (!retrievedGameSession.isPresent()) {
                logger.error("GameSession not found");
                throw new Exception("GameSession not found");
            }
            GameSession currentGameSession = retrievedGameSession.get();
            // get answer
            String answer = currentGameSession.getAnswer();
            // document user's guess
            gameEventService.createGameEvent(gameSessionId,String.format( "Player guessed %s.", guess));
                        
            // case: if they guess the answer correctly
            if (guess.equals(answer)) {
                logger.info("client guessed the answer!");
                // document outcome
                gameEventService.createGameEvent(gameSessionId,String.format( "Player won! %s was the answer.", answer));
                // send response via webSocketMessagingService
                Map<String, Object> outgoingMessage = new HashMap<>();
                outgoingMessage.put("type", "GameWon");
                outgoingMessage.put("content", "Congratulations. You won!");
                webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);
                return;
            }
            // get attempts left
            int attemptsLeft = currentGameSession.getAttempts() - 1;
            
            // if no more attempts left
            if (attemptsLeft == 0) {
                logger.info("client lost");
                // document outcome
                gameEventService.createGameEvent(gameSessionId,String.format( "Player lost. %s was the answer.", answer));
                // send game lost message
                Map<String, Object> outgoingMessage = new HashMap<>();
                outgoingMessage.put("type", "GameLost");
                outgoingMessage.put("content", "Oh no! Game over.");
                webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);
                // delete game session
                gameSessionService.delete(gameSessionId);
                return;
            }

            // if still more attempts
            
            // generate feedback
            int numberFeedback = GameUtils.getNumberFeedback(guess,answer);
            int locationFeedback = GameUtils.getLocationFeedback(guess,answer);

            logger.info("client guess incorrectly. feedback: correctNumber ({}). correctLocation ({})", numberFeedback, locationFeedback);
            
            // document outcome
            gameEventService.createGameEvent(gameSessionId, String.format( "Player guessed incorrectly. %d correct number and %d correct location. %d attempts left.", 
            numberFeedback, locationFeedback, attemptsLeft));

            // send game feed message
            Map<String, Object> outgoingMessage = new HashMap<>();
            outgoingMessage.put("type", "Feedback");
            outgoingMessage.put("correctNumber", numberFeedback);
            outgoingMessage.put("correctLocation", locationFeedback);
            outgoingMessage.put("attempts",attemptsLeft);
            webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);

            // update game session
            currentGameSession.setAttempts(attemptsLeft);
            gameSessionService.update(currentGameSession);
        } catch(Exception e) {
            logger.error("handleGuess() error: " + e.getMessage());
        }
    }

    // for learning purposes
    // receives message from client via websocket
    @MessageMapping("/greet")
    public void handleGreeting(@Payload String message) {
        logger.info("WS /greet: {}", message);
    }
}
