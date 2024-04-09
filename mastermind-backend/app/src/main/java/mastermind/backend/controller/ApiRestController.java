package mastermind.backend.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mastermind.backend.model.GameEvent;
import mastermind.backend.model.GameSession;
import mastermind.backend.service.GameEventService;
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
    private final SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(ApiRestController.class);

    public ApiRestController(
            SimpMessagingTemplate template,
            GameEventService gameEventService,
            GameSessionService gameSessionService) {
        this.template = template;
        this.gameEventService = gameEventService;
        this.gameSessionService = gameSessionService;
    }

    // creates game session
    @PostMapping("/game-session")
    public ResponseEntity<Map<String, Object>> addGameSession() {
        logger.info("REST POST: /game-session");
        
        // create new game session
        GameSession gameSession = gameSessionService.create();

        // create game event
        gameEventService.createGameEvent(gameSession.getId(), "Game started");

        // create json body
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", gameSession.getId());
        responseBody.put("attempts", gameSession.getAttempts());
        return ResponseEntity.ok(responseBody);
    }

    // for learning purposes
    // retrieves game session
    @GetMapping("/game-session/{id}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String id) {
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
        logger.info("Received POST request at: /game-event");
        logger.info("{}",requestBody);
        // extract from JSON body
        String gameSessionId = requestBody.get("gameSessionId");
        String description = requestBody.get("description");
        // create game event entry
        gameEventService.createGameEvent(gameSessionId, description);
        
        return ResponseEntity.ok("saved");
    }

    // for learning purposes
    // get game events by game session id
    @GetMapping("/game-event/game-session-id/{gameSessionId}")
    public List<GameEvent> getMethodName(@PathVariable String gameSessionId) {
        logger.info("Received GET request: /game-event/game-session-id/" + gameSessionId);
        try {
            // retrieve game event list
            List<GameEvent> gameEventList = gameEventService.getGameEventByGameSessionId(gameSessionId);
            return gameEventList;
        } catch(Exception e) {
            logger.error("Not able to retrieve game events of game session: {}", gameSessionId);
            return Collections.emptyList();
        }
    }
}
