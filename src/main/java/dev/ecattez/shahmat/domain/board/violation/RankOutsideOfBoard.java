package dev.ecattez.shahmat.domain.board.violation;

public class RankOutsideOfBoard extends InvalidPosition {

    public RankOutsideOfBoard() {
        super("Rank is outside of the board");
    }

}
