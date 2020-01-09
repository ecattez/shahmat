package dev.ecattez.shahmat.board;

public class InvalidRank extends InvalidPosition {

    public InvalidRank(int rank) {
        super(rank + " is not a valid rank");
    }

}
