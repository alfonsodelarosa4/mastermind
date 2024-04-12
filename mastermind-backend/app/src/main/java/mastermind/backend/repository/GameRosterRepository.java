package mastermind.backend.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import mastermind.backend.model.GameRoster;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameRosterRepository {
    public static final String HASH_KEY = "GameRoster";
    // 6 hour expiry time
    public static final long ttl = 6L;

    private final RedisTemplate<String, GameRoster> template;    
    private final HashOperations<String, String, GameRoster> hashOperations;

    public GameRosterRepository(RedisTemplate<String, GameRoster> template) {
        this.template = template;
        this.hashOperations = template.opsForHash();
    }

    // saves game roster to Redis
    public GameRoster save(GameRoster gameRoster) {
        hashOperations.put(gameRoster.getId(), HASH_KEY, gameRoster);
        // sets expiry time
        template.expire(gameRoster.getId(), ttl, TimeUnit.HOURS);
        return gameRoster;
    }

    // get game roster from redis
    public Optional<GameRoster> findById(String id) {
        GameRoster gameRoster = hashOperations.get(id, HASH_KEY);
        return Optional.ofNullable(gameRoster);
    }

    // delete gameRoster from redis
    public void delete(String id) {
        hashOperations.delete(HASH_KEY, id);
    }
}
