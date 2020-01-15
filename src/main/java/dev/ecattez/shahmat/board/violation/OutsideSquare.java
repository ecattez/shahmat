package dev.ecattez.shahmat.board.violation;

public class OutsideSquare extends InvalidPosition {

    public OutsideSquare() {
        super("Square is outside of the board");
    }

}
