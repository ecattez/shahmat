package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.game.GameType;

import java.util.Objects;

public class BoardInitialized implements ChessEvent {

    public final GameType gameType;

    public BoardInitialized(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardInitialized that = (BoardInitialized) o;
        return gameType == that.gameType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameType);
    }

    @Override
    public String toString() {
        return "BoardInitialized{" +
            "gameType=" + gameType +
            '}';
    }

}
