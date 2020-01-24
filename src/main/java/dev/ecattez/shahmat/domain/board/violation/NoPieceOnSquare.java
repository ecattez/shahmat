package dev.ecattez.shahmat.domain.board.violation;

import dev.ecattez.shahmat.domain.board.square.Square;

public class NoPieceOnSquare extends InvalidPosition {

    public NoPieceOnSquare(Square location) {
        super("There is no piece on " + location);
    }

}
