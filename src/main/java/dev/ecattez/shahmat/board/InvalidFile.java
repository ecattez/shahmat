package dev.ecattez.shahmat.board;

public class InvalidFile extends InvalidPosition {

    public InvalidFile(String file) {
        super(file + " is not a valid file");
    }

}
