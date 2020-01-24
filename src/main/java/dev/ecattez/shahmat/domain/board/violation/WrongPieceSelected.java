package dev.ecattez.shahmat.domain.board.violation;

import dev.ecattez.shahmat.domain.board.square.Square;

public class WrongPieceSelected extends RulesViolation {

    public WrongPieceSelected(Square location) {
        super("Piece selected is not corresponding in " + location);
    }

}
