package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PiecePositioned implements BoardEvent {

    public final Piece piece;
    public final Square position;

    public PiecePositioned(Piece piece, Square position) {
        this.piece = piece;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PiecePositioned that = (PiecePositioned) o;
        return Objects.equals(piece, that.piece) &&
            Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, position);
    }

    @Override
    public String toString() {
        return "PiecePositioned{" +
            "piece=" + piece +
            ", location=" + position +
            '}';
    }
}
