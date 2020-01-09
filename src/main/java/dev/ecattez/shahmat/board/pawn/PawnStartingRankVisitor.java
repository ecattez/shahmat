package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.PieceColorVisitor;

public class PawnStartingRankVisitor implements PieceColorVisitor<Integer> {

    @Override
    public Integer visitBlack() {
        return 7;
    }

    @Override
    public Integer visitWhite() {
        return 2;
    }

}
