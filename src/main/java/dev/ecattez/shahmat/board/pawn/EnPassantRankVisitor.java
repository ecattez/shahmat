package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.PieceColorVisitor;
import dev.ecattez.shahmat.board.Square;

public class EnPassantRankVisitor implements PieceColorVisitor<Square.Rank> {

    @Override
    public Square.Rank visitBlack() {
        return Square.Rank.FOUR;
    }

    @Override
    public Square.Rank visitWhite() {
        return Square.Rank.FIVE;
    }

}
