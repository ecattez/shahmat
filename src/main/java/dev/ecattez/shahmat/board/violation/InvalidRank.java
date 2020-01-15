package dev.ecattez.shahmat.board.violation;

public class InvalidRank extends InvalidPosition {

    public InvalidRank(int rank) {
        super(rank + " is not a valid rank");
    }

}
