package mastermind.backend.service;

import java.util.Optional;
import mastermind.backend.model.GameRoster;
import mastermind.backend.repository.GameRosterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRosterService {
    @Autowired
    private GameRosterRepository gameRosterRepository;

    private static final Logger logger = LoggerFactory.getLogger(GameRosterService.class);

    public GameRosterService(GameRosterRepository gameRosterRepository) {
        this.gameRosterRepository = gameRosterRepository;
    }

    // create game roster
    public GameRoster create(String sessionId) {
        // create game roster
        GameRoster gameRoster = new GameRoster(sessionId);
        logger.info("Created: {}", gameRoster);
        // store to redis
        gameRosterRepository.save(gameRoster);
        return gameRoster;        
    }

    // updates game roster in repository
    public void update(GameRoster gameRoster) {
        gameRosterRepository.save(gameRoster);
    }    

    // get game roster by id game session id
    public Optional<GameRoster> findByGameSessionId(String gameSessionId) {
        // get game roster
        Optional<GameRoster> existingGameRoster = gameRosterRepository.findById(gameSessionId);
        return existingGameRoster;
    }

    // delete game roster
    public void delete(String gameSessionId) {
        gameRosterRepository.delete(gameSessionId);
    }

    // adds player to game roster
    public GameRoster addPlayer(String gameSessionId, String username) throws Exception {
        // get game roster
        Optional<GameRoster> existingGameRoster = gameRosterRepository.findById(gameSessionId);
        if(!existingGameRoster.isPresent()) {
            throw new Exception(String.format("Game roster of game session %s not found.", gameSessionId));
        }
        GameRoster gameRoster = existingGameRoster.get();
        logger.info("addPlayer: the following game roster was retrieved {}", gameRoster);
        // adds player to roster
        if(!gameRoster.addPlayer(username)) throw new Exception("username already exists");
        logger.info("addPlayer: game roster now has {}: {}", username, gameRoster);
        // save game roster back to redis
        gameRosterRepository.save(gameRoster);
        return gameRoster;
    }

    // updates player readiness
    public GameRoster updatePlayer(String gameSessionId, String username, boolean isReady) throws Exception {
        // get game roster
        Optional<GameRoster> existingGameRoster = gameRosterRepository.findById(gameSessionId);
        if(!existingGameRoster.isPresent()) {
            throw new Exception(String.format("Game roster of game session %s not found.", gameSessionId));
        }
        GameRoster gameRoster = existingGameRoster.get();
        logger.info("updatePlayer: the following game roster was retrieved {}", gameRoster);
        // updates player to roster
        gameRoster.updatePlayer(username, isReady);
        logger.info("updatePlayer: game roster updated {}: {}", username, gameRoster);
        // save game roster back to redis
        gameRosterRepository.save(gameRoster);
        return gameRoster;
    }
    
}
