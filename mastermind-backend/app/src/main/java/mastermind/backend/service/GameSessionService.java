package mastermind.backend.service;

import mastermind.backend.model.GameSession;
import mastermind.backend.repository.GameSessionRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameSessionService {
    @Autowired
    private GameSessionRepository gameSessionRepository;
    @Autowired
    private RandomApiService randomApiService;

    private final int SOLO_ATTEMPTS = 10;

    private final int MULTIPLAYER_ATTEMPTS = 5;

    private static final Logger logger = LoggerFactory.getLogger(GameSessionService.class);

    public GameSessionService(
        GameSessionRepository gameSessionRepository,
        RandomApiService randomApiService
    ) {
        this.gameSessionRepository = gameSessionRepository;
        this.randomApiService = randomApiService;
    }

    // create game session
    public GameSession create(boolean multiplayer, boolean started) {
        // generate number
        String answer = randomApiService.getRandomInteger(4,0,7);
        // get attempts
        int attempts = multiplayer ? MULTIPLAYER_ATTEMPTS : SOLO_ATTEMPTS;
        // create game session
        GameSession gameSession = new GameSession(answer,attempts, multiplayer,started);
        logger.info("Created: {}", gameSession);
        // store to redis
        gameSessionRepository.save(gameSession);
        return gameSession;        
    }

    // updates game session in repository
    public void update(GameSession gameSession) {
        gameSessionRepository.save(gameSession);
    }

    // get game session by id
    public Optional<GameSession> findById(String id) {
        // get game session
        Optional<GameSession> existingGameSession = gameSessionRepository.findById(id);
        return existingGameSession;
    }

    // delete game session
    public void delete(String id) {
        gameSessionRepository.delete(id);
    }

    // decrement attempts in game session
    public void decrementAttempts(String id) throws Exception {
        // get game session
        Optional<GameSession> existingGameSession = gameSessionRepository.findById(id);
        if(!existingGameSession.isPresent()) {
            throw new Exception(String.format("Game session %s not found.", id));
        }
        GameSession gameSession = existingGameSession.get();

        int newAttempts = gameSession.getAttempts() - 1;

        gameSession.setAttempts(newAttempts);

        // update game session
        gameSessionRepository.save(gameSession);
    }    
}
