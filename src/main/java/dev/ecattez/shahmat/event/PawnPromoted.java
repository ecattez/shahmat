package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PawnPromoted implements BoardEvent {

    public final Square location;
    public final Piece promotedBy;

    public PawnPromoted(Square location, Piece promotedBy) {
        this.location = location;
        this.promotedBy = promotedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnPromoted that = (PawnPromoted) o;
        return Objects.equals(location, that.location) &&
            Objects.equals(promotedBy, that.promotedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, promotedBy);
    }

    @Override
    public String toString() {
        return "PawnPromoted{" +
            "location=" + location +
            ", promotedBy=" + promotedBy +
            '}';
    }
}
