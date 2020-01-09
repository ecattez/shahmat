package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.PieceColorVisitor;

public class PawnTradingRankVisitor implements PieceColorVisitor<Integer> {

    @Override
    public Integer visitBlack() {
        return 1;
    }

    @Override
    public Integer visitWhite() {
        return 8;
    }

}
