package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class Capture extends StandardMove {

    private final Piece captured;

    public Capture(Piece capturedBy, Square from, Square to, Piece captured) {
        super(capturedBy, from, to);
        this.captured = captured;
    }

    public Piece captured() {
        return captured;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Capture capture = (Capture) o;
        return Objects.equals(captured, capture.captured);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), captured);
    }

    @Override
    public <T> T accept(MovementVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
