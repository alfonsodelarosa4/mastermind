package mastermind.backend.service;

import java.util.List;
import java.util.Optional;
import mastermind.backend.model.GameRoster;
import mastermind.backend.model.GameTurns;
import mastermind.backend.model.Player;
import mastermind.backend.repository.GameTurnsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameTurnsService {
    @Autowired
    private GameTurnsRepository gameTurnsRepository;

    private static final Logger logger = LoggerFactory.getLogger(GameTurnsService.class);

    public GameTurnsService(
        GameTurnsRepository gameTurnsRepository
    ) {
        this.gameTurnsRepository = gameTurnsRepository;
    }

    // create game turns
    public GameTurns create(String gameSessionId, List<Player> playerList) {
        // create game turns
        GameTurns gameTurns = new GameTurns(gameSessionId, playerList);
        logger.info("Created: {}", gameTurns);
        // store to redis
        gameTurnsRepository.save(gameTurns);
        return gameTurns;
    }

    // create game turns from game roster and number of attempts
    public GameTurns createFromGameRoster(GameRoster gameRoster, int attempts) {
        // create game turns
        GameTurns gameTurns = new GameTurns(gameRoster, attempts);
        logger.info("Created: {}", gameTurns);
        // store to redis
        gameTurnsRepository.save(gameTurns);
        return gameTurns;
    }

    // updates game turns in repository
    public void update(GameTurns gameTurns) {
        gameTurnsRepository.save(gameTurns);
    }

    // get game turns by id
    public Optional<GameTurns> findByGameSessionId(String gameSessionId) {
        // get game turns
        Optional<GameTurns> existingGameTurns = gameTurnsRepository.findById(gameSessionId);
        return existingGameTurns;
    }

    // delete game turns
    public void delete(String id) {
        gameTurnsRepository.delete(id);
    }

    // returns username of current player
    public String getCurrentPlayerUsername(String gameSessionId) throws Exception {
        // get game turns
        Optional<GameTurns> existingGameTurns = gameTurnsRepository.findById(gameSessionId);
        if(!existingGameTurns.isPresent()) {
            throw new Exception(String.format("Game turns of game session %s not found.", gameSessionId));
        }
        GameTurns gameTurns = existingGameTurns.get();

        return gameTurns.getCurrentPlayerUsername();
    }

    public int decrementCurrentPlayerAttempts(String gameSessionId) throws Exception {
        // get game turns
        Optional<GameTurns> existingGameTurns = gameTurnsRepository.findById(gameSessionId);
        if(!existingGameTurns.isPresent()) {
            throw new Exception(String.format("Game turns of game session %s not found.", gameSessionId));
        }
        GameTurns gameTurns = existingGameTurns.get();

        int newAttempts = gameTurns.decrementCurrentPlayerAttempts();

        gameTurnsRepository.save(gameTurns);
        return newAttempts;
    }

    public boolean incrementTurn(String gameSessionId) throws Exception {
        // get game turns
        Optional<GameTurns> existingGameTurns = gameTurnsRepository.findById(gameSessionId);
        if(!existingGameTurns.isPresent()) {
            throw new Exception(String.format("Game turns of game session %s not found.", gameSessionId));
        }
        GameTurns gameTurns = existingGameTurns.get();

        boolean result = gameTurns.incrementTurn();

        gameTurnsRepository.save(gameTurns);


        return result;
    }    
}
