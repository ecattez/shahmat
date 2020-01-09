package dev.ecattez.shahmat.board;

public class OutsideRank extends InvalidPosition {

    public OutsideRank(int rank) {
        super(rank + " is outside of the board");
    }

}
