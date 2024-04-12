package mastermind.backend.model;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameRoster implements Serializable {
    @Id
    private String id;
    private Map<String, Boolean> players;

    public GameRoster(String gameSessionId) {
        this.id = gameSessionId;
        this.players = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Boolean> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String,Boolean> players) {
        this.players = players;
    }

    // Add a player to the roster
    public boolean addPlayer(String username) {
        // Username already exists in the map
        if (players.containsKey(username)) return false;

        // Username doesn't exist
        players.put(username, false);
        return true;
    }

    // Update the readiness of a player
    public void updatePlayer(String username, boolean isReady) {
        players.put(username, isReady);
    }

    // Convert the map to a list of Players class
    public List<Player> toPlayerList(int attempts) {
        List<Player> playerList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : players.entrySet()) {
            playerList.add(new Player(entry.getKey(), attempts));
        }
        return playerList;
    }

    // returns whether all players are ready
    public boolean areAllPlayersReady() {
        // Iterate through the map entries and check if all values are true
        for (boolean ready : players.values()) {
            if (!ready) {
                return false;
            }
        }
        // If all players are ready, return true
        return true;
    }

    @Override
    public String toString() {
        return "GameRoster{" +
                "id='" + id + '\'' +
                ", players=" + players +
                '}';
    }
}
