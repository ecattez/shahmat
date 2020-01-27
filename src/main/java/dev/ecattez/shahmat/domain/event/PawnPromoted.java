package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class PawnPromoted implements ChessEvent {

    public final Square location;
    public final PieceType promotedTo;

    public PawnPromoted(Square location, PieceType promotedTo) {
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
        return String.format("%s=%s", location, promotedTo);
    }
}
