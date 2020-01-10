package dev.ecattez.shahmat.board;

public class InvalidSquare extends InvalidPosition {

    public InvalidSquare(String square) {
        super(square + " is not a valid square");
    }

}
