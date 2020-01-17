package dev.ecattez.shahmat.game.init;

import dev.ecattez.shahmat.game.GameTypeVisitor;

public class GameInitialization implements GameTypeVisitor<BoardInitialization> {

    @Override
    public BoardInitialization visitClassical() {
        return new ClassicalBoardInitialization();
    }

}
