package dev.ecattez.shahmat.board;

public class InvalidPosition extends RuntimeException {

    public InvalidPosition(String message) {
        super(message);
    }

    public InvalidPosition(String message, Throwable cause) {
        super(message, cause);
    }

}
