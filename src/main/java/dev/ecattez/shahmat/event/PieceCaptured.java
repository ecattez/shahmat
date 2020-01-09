package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Piece;

import java.util.Objects;

public class PieceCaptured implements BoardEvent {

    public final Piece captured;
    public final Piece capturedBy;

    public PieceCaptured(Piece captured, Piece capturedBy) {
        this.captured = captured;
        this.capturedBy = capturedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceCaptured that = (PieceCaptured) o;
        return Objects.equals(captured, that.captured) &&
            Objects.equals(capturedBy, that.capturedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(captured, capturedBy);
    }

    @Override
    public String toString() {
        return "PieceCaptured{" +
            "captured=" + captured +
            ", capturedBy=" + capturedBy +
            '}';
    }
}
