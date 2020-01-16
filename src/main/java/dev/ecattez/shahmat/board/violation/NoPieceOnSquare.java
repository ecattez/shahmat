package dev.ecattez.shahmat.board.violation;

import dev.ecattez.shahmat.board.Square;

public class NoPieceOnSquare extends InvalidPosition {

    public NoPieceOnSquare(Square location) {
        super("There is no piece on " + location);
    }

}
