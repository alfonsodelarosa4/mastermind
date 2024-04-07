package mastermind.backend.controller;

import java.util.Optional;
import mastermind.backend.model.GameSession;
import mastermind.backend.repository.GameSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiRestController {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApiRestController.class);

    public ApiRestController(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
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

    // retrieves game session
    // not to be used in the game
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
}
