package dev.ecattez.shahmat.domain.command;

import dev.ecattez.shahmat.domain.game.GameType;

public class InitBoard {

    public final GameType gameType;

    public InitBoard(GameType gameType) {
        this.gameType = gameType;
    }

}
