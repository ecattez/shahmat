package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.piece.PieceColorVisitor;
import dev.ecattez.shahmat.domain.board.square.Square;

public class PawnStartingRankVisitor implements PieceColorVisitor<Square.Rank> {

    public static PawnStartingRankVisitor instance;

    public static PawnStartingRankVisitor getInstance() {
        if (instance == null) {
            instance = new PawnStartingRankVisitor();
        }
        return instance;
    }

    @Override
    public Square.Rank visitBlack() {
        return Square.Rank.SEVEN;
    }

    @Override
    public Square.Rank visitWhite() {
        return Square.Rank.TWO;
    }

}
