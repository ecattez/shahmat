package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

public class PieceCapturedEnPassant extends PieceCaptured {

    public PieceCapturedEnPassant(
        Piece captured,
        Square to,
        Piece capturedBy,
        Square from
    ) {
        super(captured, to, capturedBy, from);
    }

    @Override
    public String toString() {
        return super.toString() + "e.p";
    }
}
