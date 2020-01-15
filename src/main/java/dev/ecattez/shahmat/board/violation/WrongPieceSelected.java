package dev.ecattez.shahmat.board.violation;

import dev.ecattez.shahmat.board.Square;

public class WrongPieceSelected extends RulesViolation {

    public WrongPieceSelected(Square location) {
        super("Piece selected is not corresponding in " + location);
    }

}
