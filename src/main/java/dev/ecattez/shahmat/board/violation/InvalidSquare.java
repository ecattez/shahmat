package dev.ecattez.shahmat.board.violation;

public class InvalidSquare extends InvalidPosition {

    public InvalidSquare(String square) {
        super(square + " is not a valid square");
    }

}
