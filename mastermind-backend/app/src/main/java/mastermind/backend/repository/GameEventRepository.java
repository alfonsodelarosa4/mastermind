package mastermind.backend.repository;

import java.util.List;
import mastermind.backend.model.GameEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameEventRepository extends MongoRepository<GameEvent, String> {
    List<GameEvent> findByGameSessionId(String gameSessionId);
}
