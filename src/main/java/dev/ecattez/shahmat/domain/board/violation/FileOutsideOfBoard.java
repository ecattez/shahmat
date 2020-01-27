package dev.ecattez.shahmat.domain.board.violation;

public class FileOutsideOfBoard extends InvalidPosition {

    public FileOutsideOfBoard() {
        super("File is outside of the board");
    }

}
