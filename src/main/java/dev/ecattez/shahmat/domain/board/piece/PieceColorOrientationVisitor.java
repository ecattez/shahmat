package dev.ecattez.shahmat.domain.board.piece;

import dev.ecattez.shahmat.domain.board.Orientation;

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
