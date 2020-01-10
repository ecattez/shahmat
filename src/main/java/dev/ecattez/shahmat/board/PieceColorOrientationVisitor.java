package dev.ecattez.shahmat.board;

public class PieceColorOrientationVisitor implements PieceColorVisitor<Orientation> {

    @Override
    public Orientation visitBlack() {
        return Orientation.DOWNWARD;
    }

    @Override
    public Orientation visitWhite() {
        return Orientation.UPWARD;
    }

}
