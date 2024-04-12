package mastermind.backend.controller;

import java.util.Map;
import java.util.Optional;
import mastermind.backend.model.GameSession;
import mastermind.backend.model.GameRoster;
import mastermind.backend.service.GameEventService;
import mastermind.backend.service.GameManagementService;
import mastermind.backend.service.GameRosterService;
import mastermind.backend.service.GameSessionService;
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
    private GameRosterService gameRosterService;
    @Autowired
    private GameManagementService gameManagementService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    public WebSocketController(
           GameSessionService gameSessionService,
           GameEventService gameEventService,
           GameRosterService gameRosterService) {
        this.gameSessionService = gameSessionService;
        this.gameEventService = gameEventService;
        this.gameRosterService = gameRosterService;
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
            GameSession gameSession = retrievedGameSession.get();

            // Create username
            String username = "Player";
            if(gameSession.getMultiplayer()) {
                username = receivedMessage.get("username");
            }

            // document user's guess
            gameEventService.create(gameSessionId,String.format( "%s guessed %s.", username, guess));
            
            // if multiplayer broadcast guess
            if(gameSession.getMultiplayer()) gameManagementService.broadcastGuess(guess,gameSessionId,username);
            
            // get answer
            String answer = gameSession.getAnswer();

            // check if guess was correct
            if (guess.equals(answer)) {
                gameManagementService.gameWon(guess, gameSession, username);
                return;
            }

            // send feedback from answer
            boolean areThereMoreAttempts = gameManagementService.manageFeedback(guess, gameSession, username);

            // if no more attempts left
            if (!areThereMoreAttempts) {
                gameManagementService.gameLost(gameSession);
                return;
            }

            // if multiplayer broadcast next person's turn
            if (gameSession.getMultiplayer()) {
                gameManagementService.broadcastPlayerTurn(gameSessionId);
            }

        } catch(Exception e) {
            logger.error("handleGuess() error: " + e.getMessage());
        }
    }

    @MessageMapping("/ready")
    public void handleReady(@Payload Map<String, Object> receivedMessage) {
        try {
            logger.info("WS /ready: {}", receivedMessage);

            // Access the values from the request body using keys
            String gameSessionId = (String) receivedMessage.get("gameSessionId");
            String username = (String) receivedMessage.get("username");
            Boolean ready = (boolean) receivedMessage.get("ready");

            // update player status
            GameRoster gameRoster = gameRosterService.updatePlayer(gameSessionId,username,ready);

            // broadcast new game roster
            gameManagementService.checkIfAllReady(gameRoster);
        } catch(Exception e) {
            logger.error("handleReady() error: " + e.getMessage());
        }
    }

    // for learning purposes
    // receives message from client via websocket
    @MessageMapping("/greet")
    public void handleGreeting(@Payload String message) {
        logger.info("WS /greet: {}", message);
    }
}
