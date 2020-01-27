package dev.ecattez.shahmat.domain.game.init;

import dev.ecattez.shahmat.domain.game.GameTypeVisitor;

public class GameInitialization implements GameTypeVisitor<BoardInitialization> {

    @Override
    public BoardInitialization visitClassical() {
        return new ClassicalBoardInitialization();
    }

}
