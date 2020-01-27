package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.piece.PieceColorVisitor;
import dev.ecattez.shahmat.domain.board.square.Square;

public class EnPassantCaptureRankVisitor implements PieceColorVisitor<Square.Rank> {

    @Override
    public Square.Rank visitBlack() {
        return Square.Rank.FOUR;
    }

    @Override
    public Square.Rank visitWhite() {
        return Square.Rank.FIVE;
    }

}
