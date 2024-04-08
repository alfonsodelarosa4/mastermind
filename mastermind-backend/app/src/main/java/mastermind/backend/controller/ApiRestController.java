package mastermind.backend.controller;

import java.util.Map;
import java.util.Optional;
import mastermind.backend.model.GameSession;
import mastermind.backend.repository.GameSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiRestController {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private final SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(ApiRestController.class);

    public ApiRestController(GameSessionRepository gameSessionRepository, SimpMessagingTemplate template) {
        this.gameSessionRepository = gameSessionRepository;
        this.template = template;
    }

    // creates game session
    @PostMapping("/game-session")
    public ResponseEntity<GameSession> addGameSession() {
        logger.info("REST POST: /game-session");
        GameSession gameSession = new GameSession("1234");
        logger.info("Created: {}", gameSession);
        gameSessionRepository.save(gameSession);
        return ResponseEntity.ok(gameSession);
    }

    // for learning purposes
    // retrieves game session
    @GetMapping("/game-session/{id}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable String id) {
        logger.info("REST GET: /game-session");
        Optional<GameSession> existingGameSession = gameSessionRepository.findById(id);

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
        // Extract from JSON body
        String topic = requestBody.get("game-session");
        String message = requestBody.get("message");

        template.convertAndSend(topic,message);
        
        // Return the response
        return ResponseEntity.ok("Message send over websocket");
    }
}
