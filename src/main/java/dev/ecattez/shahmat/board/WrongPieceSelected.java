package dev.ecattez.shahmat.board;

public class WrongPieceSelected extends RulesViolation {

    public WrongPieceSelected(Square location) {
        super("Piece selected is not corresponding in " + location);
    }

}
