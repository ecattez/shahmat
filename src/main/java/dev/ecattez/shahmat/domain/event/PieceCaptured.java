package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class PieceCaptured extends ChessMoveEvent {

    public final Piece captured;

    public PieceCaptured(
        Piece captured,
        Square to,
        Piece capturedBy,
        Square from
    ) {
        super(capturedBy, from, to);
        this.captured = captured;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PieceCaptured that = (PieceCaptured) o;
        return Objects.equals(captured, that.captured);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), captured);
    }

    @Override
    public String toString() {
        if (piece.isOfType(PieceType.PAWN)) {
            return String.format("%sx%s", from.file, to);
        }
        return String.format("%sx%s", piece, to);
    }
}
