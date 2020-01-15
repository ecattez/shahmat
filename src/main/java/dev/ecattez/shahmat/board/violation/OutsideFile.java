package dev.ecattez.shahmat.board.violation;

public class OutsideFile extends InvalidPosition {

    public OutsideFile(String file) {
        super(file + " is outside of the board");
    }

}
