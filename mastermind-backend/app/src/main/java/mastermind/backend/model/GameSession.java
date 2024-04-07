package mastermind.backend.model;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Random;

public class GameSession implements Serializable {

    @Id
    private String id;
    private String answer;

    public GameSession() {
        this.id = generateId();
    }
    
    public GameSession(String answer) {
        this();
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // generates random id
    private String generateId() {
        int length = 8;
        String values = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String generatedId = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int nextIndex = random.nextInt(values.length());
            char nextCharacter = values.charAt(nextIndex);
            generatedId += nextCharacter;
        }
        return generatedId;
    }

    @Override
    public String toString() {
        return String.format("GameSession{id=%s, answer=%s}", id, answer);
    }    
}
