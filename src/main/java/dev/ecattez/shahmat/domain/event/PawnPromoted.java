package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;

import java.util.Objects;

public class PawnPromoted extends ChessMoveEvent {

    public final ChessMoveEvent move;

    public PawnPromoted(ChessMoveEvent move, Piece promotedTo) {
        super(promotedTo, move.from, move.to);
        this.move = move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PawnPromoted that = (PawnPromoted) o;
        return Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), move);
    }

    @Override
    public String toString() {
        return String.format("%s=%s", move, piece);
    }
}
