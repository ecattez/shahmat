package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.piece.PieceColorVisitor;
import dev.ecattez.shahmat.domain.board.square.Square;

public class PawnPromotionRankVisitor implements PieceColorVisitor<Square.Rank> {

    public static PawnPromotionRankVisitor instance;

    public static PawnPromotionRankVisitor getInstance() {
        if (instance == null) {
            instance = new PawnPromotionRankVisitor();
        }
        return instance;
    }

    @Override
    public Square.Rank visitBlack() {
        return Square.Rank.ONE;
    }

    @Override
    public Square.Rank visitWhite() {
        return Square.Rank.EIGHT;
    }

}
