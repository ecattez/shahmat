package dev.ecattez.shahmat.game;

public class BoardAlreadyInitialized extends IllegalArgumentException {

    public BoardAlreadyInitialized() {
        super("The board is already initialized");
    }

}
