package dev.ecattez.shahmat.board.move;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

public class EnPassant extends Capture {

    public EnPassant(Piece capturedBy, Square from, Square to, Piece captured) {
        super(capturedBy, from, to, captured);
    }

    @Override
    public <T> T accept(MovementVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
