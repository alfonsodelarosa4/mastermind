package mastermind.backend.service;

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
    public void create(String gameSessionId, String description) throws Exception {
        logger.info("creating game event");
        try {
            GameEvent gameEvent = new GameEvent(gameSessionId, description);
            gameEventRepository.save(gameEvent);
            logger.info("created: {}", gameEvent);
        } catch(Exception e) {
            throw new Exception("createGameEvent() error: " + e.getMessage());
        }
    }

    // get game events by game session id
    public List<GameEvent> getByGameSessionId(String gameSessionId) throws Exception{
        try {
            logger.info("retrieving game events of game session: {}",gameSessionId);
            List<GameEvent> gameEventList = gameEventRepository.findByGameSessionId(gameSessionId);
            return gameEventList;
        } catch(Exception e) {
            throw new Exception("Not able to retrieve game events of game session: " + gameSessionId);
        }
    }

    public void deleteByGameSessionId(String gameSessionId) throws Exception {
        try {
            logger.info("retrieving game events of game session: {}",gameSessionId);
            gameEventRepository.deleteByGameSessionId(gameSessionId);
        } catch(Exception e) {
            throw new Exception("Not able to delete game events of game session: " + gameSessionId);
        }
    }
}
