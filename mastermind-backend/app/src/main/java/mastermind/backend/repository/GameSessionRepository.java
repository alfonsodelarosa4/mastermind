package mastermind.backend.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import mastermind.backend.model.GameSession;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameSessionRepository {

    public static final String HASH_KEY = "GameSession";
    // 6 hour expiry time
    public static final long ttl = 6L;

    private final RedisTemplate<String, GameSession> template;    
    private final HashOperations<String, String, GameSession> hashOperations;

    public GameSessionRepository(RedisTemplate<String, GameSession> template) {
        this.template = template;
        this.hashOperations = template.opsForHash();
    }

    // saves GameSession to Redis
    public GameSession save(GameSession gameSession) {
        hashOperations.put(gameSession.getId(), HASH_KEY, gameSession);
        // sets expiry time
        template.expire(gameSession.getId(), ttl, TimeUnit.HOURS);
        return gameSession;
    }

    // get GameSession from redis
    public Optional<GameSession> findById(String id) {
        GameSession gameSession = hashOperations.get(id, HASH_KEY);
        return Optional.ofNullable(gameSession);
    }    
}
