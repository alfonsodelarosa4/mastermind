package mastermind.backend.model;

import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.List;

public class GameTurns implements Serializable {
    @Id
    private String id;
    private int turnIndex;
    private List<Player> playerList;

    public GameTurns(String gameSessionId, List<Player> playerList) {
        this.id = gameSessionId;
        this.playerList = playerList;
        this.turnIndex = 0;
    }

    public GameTurns(GameRoster gameRoster, int attempts) {
        this(gameRoster.getId(),gameRoster.toPlayerList(attempts));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void setTurnIndex(int turnIndex) {
        this.turnIndex = turnIndex;
    }

    public String getCurrentPlayerUsername() {
        return playerList.get(turnIndex).getUsername();
    }

    public int decrementCurrentPlayerAttempts() {
        int newAttempts = playerList.get(turnIndex).getAttempts() - 1;
        playerList.get(turnIndex).setAttempts(newAttempts);
        return newAttempts;
    }

    public boolean incrementTurn() {
        int nextTurnIndex = (turnIndex + 1) % playerList.size();

        setTurnIndex(nextTurnIndex);

        int attemptsLeftOfNextPlayer = playerList.get(turnIndex).getAttempts();

        return attemptsLeftOfNextPlayer > 0;
    }

    @Override
    public String toString() {
        return "GameTurns{" +
                "id='" + id + '\'' +
                ", turnIndex=" + turnIndex +
                ", playerList=" + playerList +
                '}';
    }

}
