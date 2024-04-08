package mastermind.backend.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GameEvent")
public class GameEvent {

    @Id
    private String id;
    private String gameSessionId;
    private String timestamp;
    public String description;

    public GameEvent() {
        this.timestamp = Long.toString(new Date().getTime());
    }

    public GameEvent(String gameSessionId, String description) {
        this();
        this.gameSessionId = gameSessionId;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("GameEvent[id=%s, gameSessionId=%s, timestamp=%s, description=%s]",
            id, gameSessionId, timestamp, description);
    }    
}
