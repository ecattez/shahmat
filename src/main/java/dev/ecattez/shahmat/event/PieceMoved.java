package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PieceMoved implements BoardEvent {

    public final Piece piece;
    public final Square from;
    public final Square to;

    public PieceMoved(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMoved that = (PieceMoved) o;
        return Objects.equals(piece, that.piece) &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, to);
    }

    @Override
    public String toString() {
        return "PieceMoved{" +
            "piece=" + piece +
            ", from=" + from +
            ", to=" + to +
            '}';
    }
}
