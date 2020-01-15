package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class PieceCaptured implements BoardEvent {

    public final Piece captured;
    public final Piece capturedBy;
    public final Square location;

    public PieceCaptured(Piece captured, Piece capturedBy, Square location) {
        this.captured = captured;
        this.capturedBy = capturedBy;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceCaptured that = (PieceCaptured) o;
        return Objects.equals(captured, that.captured) &&
            Objects.equals(capturedBy, that.capturedBy) &&
            Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(captured, capturedBy, location);
    }

    @Override
    public String toString() {
        return "PieceCaptured{" +
            "captured=" + captured +
            ", capturedBy=" + capturedBy +
            ", location=" + location +
            '}';
    }
}
