package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public abstract class ChessMoveEvent implements ChessEvent {

    public final Piece piece;
    public final Square from;
    public final Square to;

    public ChessMoveEvent(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveEvent that = (ChessMoveEvent) o;
        return Objects.equals(piece, that.piece) &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, to);
    }

}
