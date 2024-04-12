package mastermind.backend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mastermind.backend.model.GameRoster;
import mastermind.backend.model.GameSession;
import mastermind.backend.utils.GameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameManagementService {

    @Autowired
    private final WebSocketMessagingService webSocketMessagingService;
    @Autowired
    private final GameTurnsService gameTurnsService;
    @Autowired
    private final GameEventService gameEventService;
    @Autowired
    private final GameSessionService gameSessionService;
    @Autowired
    private final GameRosterService gameRosterService;

    private final int ATTEMPTS_IN_MULTIPLAYER = 5;

    private static final Logger logger = LoggerFactory.getLogger(GameManagementService.class);

    public GameManagementService(
        WebSocketMessagingService webSocketMessagingService,
        GameTurnsService gameTurnsService,
        GameEventService gameEventService,
        GameSessionService gameSessionService,
        GameRosterService gameRosterService
    ) {
        this.webSocketMessagingService = webSocketMessagingService;
        this.gameTurnsService = gameTurnsService;
        this.gameEventService = gameEventService;
        this.gameSessionService = gameSessionService;
        this.gameRosterService = gameRosterService;
    }

    // broadcasts lists of players to game session
    public void broadcastPlayerRoster(GameRoster gameRoster) throws Exception{
        logger.info("broadcastPlayerRoster(): game session {}", gameRoster.getId());
        // get list of players
        Map<String,Boolean> playerList = gameRoster.getPlayers();
        
        // create hashmap of players
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type", "Roster");
        outgoingMessage.put("roster", playerList);

        // send roster message to other player
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameRoster.getId());
    }

    // if not all players are ready, broadcast player roster
    // if all players are ready, create gameTurns object and broadcast game start
    public void checkIfAllReady(GameRoster gameRoster) throws Exception {
        if (gameRoster.areAllPlayersReady()) {
            gameTurnsService.createFromGameRoster(gameRoster, ATTEMPTS_IN_MULTIPLAYER);
            broadcastGameStart(gameRoster);            
        } else {
            broadcastPlayerRoster(gameRoster);
        }
    }

    // broadcast game start to game session and current turn
    private void broadcastGameStart(GameRoster gameRoster) throws Exception {
        logger.info("broadcastIfAllReady(): all players in game session {} are ready", gameRoster.getId());
        // get list of players
        Map<String,Boolean> playerList = gameRoster.getPlayers();
        // get current player's turn
        String currentTurn = gameTurnsService.getCurrentPlayerUsername(gameRoster.getId());
        // create hashmap of players
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type", "GameStart");
        outgoingMessage.put("currentTurn",currentTurn);
        outgoingMessage.put("roster", playerList);

        // send roster message to other player
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameRoster.getId());
    }

    // broadcast current players turn
    public void broadcastPlayerTurn(String gameSessionId) throws Exception {
        logger.info("broadcastPlayerTurn(): game session {}", gameSessionId);
        // get username of current player
        String username = gameTurnsService.getCurrentPlayerUsername(gameSessionId);

        // create hashmap of players
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type", "NextTurn");
        outgoingMessage.put("currentTurn", username);

        // send next turn of player
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);
    }

    // broadcast game won and delete game
    public void gameWon(String guess, GameSession gameSession, String username) throws Exception {
        logger.info("gameWon(): {} guessed the answer in game session {}!",username,gameSession.getId());
        // document outcome
        gameEventService.create(gameSession.getId(),String.format( "%s won! %s was the answer.", username, gameSession.getAnswer()));
        // send response via webSocketMessagingService
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type", "GameWon");
        if (gameSession.getMultiplayer()) {
            outgoingMessage.put("username", username);
        }
        outgoingMessage.put("content", String.format( "%s was the answer.", gameSession.getAnswer()));
        
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSession.getId());

        // delete game data
        deleteGameData(gameSession.getId());
    }

    // broadcast guess to game session
    public void broadcastGuess(String guess, String gameSessionId, String username) {
        logger.info("broadcastGuess(): {}'s guess in games session {}: {}", username,gameSessionId, guess);
        // send guess via webSocketMessagingService
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type","PlayerGuess");
        outgoingMessage.put("username",username);
        outgoingMessage.put("guess", guess);
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);
    }

    // broadcast feedback
    public boolean manageFeedback(String guess, GameSession gameSession, String username) throws Exception {
        // get answer
        String answer = gameSession.getAnswer();
        // get game session id
        String gameSessionId = gameSession.getId();

        // get attempts left
        // solo: decrement attempts of user
        // multi: decrement attempts of player who just guessed and change turn to next person
        int attemptsLeft; 
        boolean areThereMoreAttempts;
        if (gameSession.getMultiplayer()) {
            attemptsLeft = gameTurnsService.decrementCurrentPlayerAttempts(gameSessionId);
            areThereMoreAttempts = gameTurnsService.incrementTurn(gameSessionId);
        } else {            
            attemptsLeft = gameSession.getAttempts() - 1;
            areThereMoreAttempts = attemptsLeft > 0;
            gameSessionService.decrementAttempts(gameSessionId);
        }
        
        // generate feedback
        int numberFeedback = GameUtils.getNumberFeedback(guess,answer);
        int locationFeedback = GameUtils.getLocationFeedback(guess,answer);
        String feedback = String.format("%s guessed incorrectly. %d correct numbers and %d correct locations.",username, numberFeedback, locationFeedback);

        logger.info(feedback);
        
        // document feedback as game event
        gameEventService.create(gameSessionId, feedback);

        // send feedback message
        // solo: send feedback and send new attempts of user
        // multiplayer: send feedback and send new attempts of that user
        Map<String, Object> outgoingMessage = new HashMap<>();
        outgoingMessage.put("type", "Feedback");
        outgoingMessage.put("content", feedback);
        if(gameSession.getMultiplayer()) outgoingMessage.put("username", username);
        outgoingMessage.put("attempts",attemptsLeft);
        webSocketMessagingService.sendMessageToGameSession(outgoingMessage, gameSessionId);

        return areThereMoreAttempts;
    }

    // broadcast game lost and delete game session
    public void gameLost(GameSession gameSession) throws Exception {
        logger.info("client lost");
        // document outcome
        gameEventService.create(gameSession.getId(),String.format( "Game lost. %s was the answer.", gameSession.getAnswer()));
        // send game lost message
        Map<String, Object> endMessage = new HashMap<>();
        endMessage.put("type", "GameLost");
        if (gameSession.getMultiplayer()) {
            endMessage.put("content", "There was no winner");
        } else {
            endMessage.put("content", "Oh no! Game over.");
        }
        webSocketMessagingService.sendMessageToGameSession(endMessage, gameSession.getId());
        // delete game session
        deleteGameData(gameSession.getId());
    }

    // delete game data
    public void deleteGameData(String gameSessionId) throws Exception {
        // wait 1 minute
        TimeUnit.MINUTES.sleep(1);
        // delete all game data
        gameSessionService.delete(gameSessionId);
        gameRosterService.delete(gameSessionId);
        gameTurnsService.delete(gameSessionId);
        gameEventService.deleteByGameSessionId(gameSessionId);
    }
}
