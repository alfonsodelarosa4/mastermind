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

    private static final Logger logger = LoggerFactory.getLogger(GameEventService.class);

    public GameSessionService(
        GameSessionRepository gameSessionRepository,
        RandomApiService randomApiService
    ) {
        this.gameSessionRepository = gameSessionRepository;
        this.randomApiService = randomApiService;
    }

    // create game session
    public GameSession create() {
        // generate number
        String answer = randomApiService.getRandomInteger(4,0,7);
        // create game session
        GameSession gameSession = new GameSession(answer,10);
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
    
}
