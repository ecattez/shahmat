package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class PieceCaptured implements ChessEvent {

    public final Piece captured;
    public final Square to;
    public final Piece capturedBy;
    public final Square from;

    public PieceCaptured(
        Piece captured,
        Square to,
        Piece capturedBy,
        Square from
    ) {
        this.captured = captured;
        this.to = to;
        this.capturedBy = capturedBy;
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceCaptured that = (PieceCaptured) o;
        return Objects.equals(captured, that.captured) &&
            Objects.equals(to, that.to) &&
            Objects.equals(capturedBy, that.capturedBy) &&
            Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(captured, to, capturedBy, from);
    }

    @Override
    public String toString() {
        if (capturedBy.isOfType(PieceType.PAWN)) {
            return String.format("%sx%s", from.file, to);
        }
        return String.format("%sx%s", capturedBy, to);
    }
}
