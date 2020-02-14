package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class StandardMove implements Movement {

    private final Piece piece;
    private final Square from;
    private final Square to;

    public StandardMove(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

    @Override
    public Piece piece() {
        return piece;
    }

    @Override
    public Square from() {
        return from;
    }

    @Override
    public Square to() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardMove that = (StandardMove) o;
        return Objects.equals(piece, that.piece) &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, to);
    }

    @Override
    public <T> T accept(MovementVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
