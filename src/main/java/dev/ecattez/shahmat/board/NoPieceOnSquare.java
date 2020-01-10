package dev.ecattez.shahmat.board;

public class NoPieceOnSquare extends InvalidPosition {

    public NoPieceOnSquare(Square location) {
        super("There is no piece on " + location);
    }

}
