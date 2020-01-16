package dev.ecattez.shahmat.board.violation;

public class SquareOutsideOfBoard extends InvalidPosition {

    public SquareOutsideOfBoard() {
        super("Square is outside of the board");
    }

}
