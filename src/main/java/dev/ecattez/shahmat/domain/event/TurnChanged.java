package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.PieceColor;

import java.util.Objects;

public class TurnChanged implements ChessEvent {

    public final PieceColor color;

    public TurnChanged(PieceColor color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnChanged that = (TurnChanged) o;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public String toString() {
        return "TurnChanged{" +
            "color=" + color +
            '}';
    }
}
