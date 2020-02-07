package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class KingChecked implements ChessEvent {

    public final ChessMoveEvent move;
    public final Square kingLocation;

    public KingChecked(ChessMoveEvent move, Square kingLocation) {
        this.move = move;
        this.kingLocation = kingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KingChecked that = (KingChecked) o;
        return Objects.equals(move, that.move) &&
            Objects.equals(kingLocation, that.kingLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, kingLocation);
    }

    @Override
    public String toString() {
        return move.toString() + "+";
    }

}
