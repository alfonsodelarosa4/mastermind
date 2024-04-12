package mastermind.backend.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mastermind.backend.model.GameEvent;
import mastermind.backend.model.GameRoster;
import mastermind.backend.model.GameSession;
import mastermind.backend.service.GameEventService;
import mastermind.backend.service.GameManagementService;
import mastermind.backend.service.GameRosterService;
import mastermind.backend.service.GameSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiRestController {

    @Autowired
    private GameEventService gameEventService;
    @Autowired
    private GameSessionService gameSessionService;
    @Autowired
    private GameRosterService gameRosterService;
    @Autowired
    private GameManagementService gameManagementService;
    @Autowired
    private final SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(ApiRestController.class);

    public ApiRestController(
            SimpMessagingTemplate template,
            GameEventService gameEventService,
            GameSessionService gameSessionService,
            GameRosterService gameRosterService,
            GameManagementService gameManagementService) {
        this.template = template;
        this.gameEventService = gameEventService;
        this.gameSessionService = gameSessionService;
        this.gameRosterService = gameRosterService;
        this.gameManagementService = gameManagementService;
    }

    // creates game session
    @PostMapping("/game-session")
    public ResponseEntity<Map<String, Object>> createGameSession(@RequestBody Map<String, Object> requestBody) {
        try {
            logger.info("REST POST: /game-session");
            // Access the values from the request body using keys
            boolean multiplayer = (boolean) requestBody.get("multiplayer");
    
            // create new game session
            GameSession gameSession = gameSessionService.create(multiplayer,!multiplayer);
    
            // create json body
            Map<String, Object> responseBody = new HashMap<>();
    
            // if multiplayer
            if(multiplayer) {
                String username = (String) requestBody.get("username");
                // create game event
                gameEventService.create(gameSession.getId(), "Game created");
                // create game roster
                gameRosterService.create(gameSession.getId());
                // add current player to roster
                GameRoster gameRoster = gameRosterService.addPlayer(gameSession.getId(), username);
                // provide username
                responseBody.put("username", username);
                // joining players to response body
                responseBody.put("roster",gameRoster.getPlayers());
            } else {                
                // create game event
                gameEventService.create(gameSession.getId(), "Game started");
            }
            
            responseBody.put("attempts", gameSession.getAttempts());
            responseBody.put("id", gameSession.getId());
            responseBody.put("multiplayer", multiplayer);
            return ResponseEntity.ok(responseBody);
        } catch(Exception e) {
            logger.error("/game-session: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } 
    }

    // creates game session
    @PostMapping("/join-game-session")
    public ResponseEntity<Map<String, Object>> joinGameSession(@RequestBody Map<String, Object> requestBody) {
        try {
            logger.info("REST POST: /join-game-session");

            String gameSessionId = (String) requestBody.get("gameSessionId");
            String username = (String) requestBody.get("username");

            // get game session
            Optional<GameSession> existingGameSession = gameSessionService.findById(gameSessionId);
            if (!existingGameSession.isPresent()) {
                logger.info("GameSession not found");
                throw new Exception("Games session not found");
            }
            GameSession gameSession = existingGameSession.get();
         
            // add new player to roster
            GameRoster gameRoster = gameRosterService.addPlayer(gameSessionId, username);

            // broadcast message of new roster
            gameManagementService.checkIfAllReady(gameRoster);

            // create json body
            Map<String, Object> responseBody = new HashMap<>();
            
            // joining players to response body
            // gameSession
            responseBody.put("id", gameSessionId);
            responseBody.put("multiplayer", true);
            // partyGame
            responseBody.put("roster",gameRoster.getPlayers());
            responseBody.put("attempts", gameSession.getAttempts());
            responseBody.put("username", username);
            return ResponseEntity.ok(responseBody);
        } catch(Exception e) {
            logger.error("/join-game-session: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } 
    }

    // for learning purposes
    // retrieves game session
    @GetMapping("/game-session/{id}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String id) {
        try {
            logger.info("REST GET: /game-session");
            // get game session
            Optional<GameSession> existingGameSession = gameSessionService.findById(id);
    
            if (existingGameSession.isPresent()) {
                logger.info("GameSession found: {}", existingGameSession.get());
                return ResponseEntity.ok().body(existingGameSession.get());
            } else {
                logger.info("GameSession not found");
                return ResponseEntity.notFound().build();
            }
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // for learning purposes
    // sends message via WebSocket
    @PostMapping("/ws-chat")
    public ResponseEntity<String> sendWSMessage(@RequestBody Map<String, String> requestBody) {
        logger.info("REST POST: /ws-chat");

        // extract from JSON body
        String topic = requestBody.get("gameSession");
        String message = requestBody.get("message");
        // send message via topic
        template.convertAndSend(topic,message);
        
        // return the response
        return ResponseEntity.ok("Message send over websocket");
    }

    // for learning purposes
    // creates game event
    @PostMapping("/game-event")
    public ResponseEntity<String> addImageMetadata (@RequestBody Map<String, String> requestBody) {
        try {
            logger.info("Received POST request at: /game-event");
            logger.info("{}",requestBody);
            // extract from JSON body
            String gameSessionId = requestBody.get("gameSessionId");
            String description = requestBody.get("description");
            // create game event entry
            gameEventService.create(gameSessionId, description);
            return ResponseEntity.ok("saved");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().build();
        }        
    }

    // for learning purposes
    // get game events by game session id
    @GetMapping("/game-event/game-session-id/{gameSessionId}")
    public List<GameEvent> getGameEventByGameSessionId(@PathVariable String gameSessionId) {
        try {
            logger.info("Received GET request: /game-event/game-session-id/" + gameSessionId);
            // retrieve game event list
            List<GameEvent> gameEventList = gameEventService.getByGameSessionId(gameSessionId);
            return gameEventList;
        } catch(Exception e) {
            logger.error("Not able to retrieve game events of game session: {}", gameSessionId);
            return Collections.emptyList();
        }
    }
}
