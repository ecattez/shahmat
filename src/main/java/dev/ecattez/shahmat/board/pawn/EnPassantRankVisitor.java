package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.PieceColorVisitor;

public class EnPassantRankVisitor implements PieceColorVisitor<Integer> {

    @Override
    public Integer visitBlack() {
        return 4;
    }

    @Override
    public Integer visitWhite() {
        return 5;
    }

}
