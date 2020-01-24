package dev.ecattez.shahmat.domain.board.violation;

public class BoardAlreadyInitialized extends IllegalArgumentException {

    public BoardAlreadyInitialized() {
        super("The board is already initialized");
    }

}
