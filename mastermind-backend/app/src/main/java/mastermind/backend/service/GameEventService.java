package mastermind.backend.service;

import java.util.Collections;
import java.util.List;
import mastermind.backend.model.GameEvent;
import mastermind.backend.repository.GameEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameEventService {

    @Autowired
    private GameEventRepository gameEventRepository;

    private static final Logger logger = LoggerFactory.getLogger(GameEvent.class);

    public GameEventService(GameEventRepository gameEventRepository) {
        this.gameEventRepository = gameEventRepository;
    }

    // creates and saves game event to MongoDB
    public void createGameEvent(String gameSessionId, String description) {
        logger.info("creating game event");
        try {
            GameEvent gameEvent = new GameEvent(gameSessionId, description);
            gameEventRepository.save(gameEvent);
            logger.info("created: {}", gameEvent);
        } catch(Exception e) {
            logger.error("createGameEvent() error: " + e);
        }
    }

    // get game events by game session id
    public List<GameEvent> getGameEventByGameSessionId(String gameSessionId) {
        logger.info("retrieving game events of game session: {}",gameSessionId);
        try {
            List<GameEvent> gameEventList = gameEventRepository.findByGameSessionId(gameSessionId);
            return gameEventList;
        } catch(Exception e) {
            logger.error("Not able to retrieve game events of game session: {}", gameSessionId);
            return Collections.emptyList();
        }
    }
    
}
