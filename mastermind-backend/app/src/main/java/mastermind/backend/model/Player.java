package mastermind.backend.model;

import java.io.Serializable;

public class Player implements Serializable {
    private String username;
    private int attempts;

    public Player(String username, int attempts) {
        this.username = username;
        this.attempts = attempts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", attempts=" + attempts +
                '}';
    }
}

