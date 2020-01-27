package dev.ecattez.shahmat.domain.board.violation;

import dev.ecattez.shahmat.domain.board.square.Square;

public class PieceNotOwned extends RulesViolation {

    public PieceNotOwned(Square from) {
        super("Piece in " + from + " is not owned");
    }

}
