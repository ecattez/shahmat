package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.game.GameType;

public class InitBoard {

    public final GameType gameType;

    public InitBoard(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public String toString() {
        return "InitBoard{" +
            "gameType=" + gameType +
            '}';
    }
}
