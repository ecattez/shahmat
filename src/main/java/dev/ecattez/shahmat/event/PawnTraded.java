package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PawnTraded implements BoardEvent {

    public final Square location;
    public final Piece tradedBy;

    public PawnTraded(Square location, Piece tradedBy) {
        this.location = location;
        this.tradedBy = tradedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PawnTraded that = (PawnTraded) o;
        return Objects.equals(location, that.location) &&
            Objects.equals(tradedBy, that.tradedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, tradedBy);
    }

    @Override
    public String toString() {
        return "PawnTraded{" +
            "location=" + location +
            ", tradedBy=" + tradedBy +
            '}';
    }
}
