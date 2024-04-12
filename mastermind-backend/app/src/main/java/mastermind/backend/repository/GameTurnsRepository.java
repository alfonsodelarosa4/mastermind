package mastermind.backend.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import mastermind.backend.model.GameTurns;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameTurnsRepository {
    
    public static final String HASH_KEY = "GameTurns";
    // 6 hour expiry time
    public static final long ttl = 6L;

    private final RedisTemplate<String, GameTurns> template;    
    private final HashOperations<String, String, GameTurns> hashOperations;

    public GameTurnsRepository(RedisTemplate<String, GameTurns> template) {
        this.template = template;
        this.hashOperations = template.opsForHash();
    }

    // saves GameTurns to Redis
    public GameTurns save(GameTurns GameTurns) {
        hashOperations.put(GameTurns.getId(), HASH_KEY, GameTurns);
        // sets expiry time
        template.expire(GameTurns.getId(), ttl, TimeUnit.HOURS);
        return GameTurns;
    }

    // get GameTurns from redis
    public Optional<GameTurns> findById(String id) {
        GameTurns GameTurns = hashOperations.get(id, HASH_KEY);
        return Optional.ofNullable(GameTurns);
    }

    // delete GameTurns from redis
    public void delete(String id) {
        hashOperations.delete(HASH_KEY, id);
    }
}
