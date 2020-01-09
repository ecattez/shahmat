package dev.ecattez.shahmat.board;

public class InvalidSquare extends InvalidPosition {

    public InvalidSquare(String square) {
        super(square + " is not a valid square");
    }

    public InvalidSquare(String square, Throwable cause) {
        super(square + " is not a valid square", cause);
    }

}
