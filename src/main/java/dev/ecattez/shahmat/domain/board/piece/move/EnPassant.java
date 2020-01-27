package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

public class EnPassant extends Capture {

    public EnPassant(Piece capturedBy, Square from, Square to, Piece captured) {
        super(capturedBy, from, to, captured);
    }

    @Override
    public <T> T accept(MovementVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
