package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PawnPromoted implements BoardEvent {

    public final Square location;
    public final Piece promotedTo;

    public PawnPromoted(Square location, Piece promotedTo) {
        this.location = location;
        this.promotedTo = promotedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnPromoted that = (PawnPromoted) o;
        return Objects.equals(location, that.location) &&
            Objects.equals(promotedTo, that.promotedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, promotedTo);
    }

    @Override
    public String toString() {
        return "PawnPromoted{" +
            "location=" + location +
            ", promotedTo=" + promotedTo +
            '}';
    }
}
