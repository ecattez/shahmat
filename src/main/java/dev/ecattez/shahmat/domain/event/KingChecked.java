package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class KingChecked implements ChessEvent {

    public final ChessEvent cause;
    public final Square kingLocation;

    public KingChecked(ChessEvent move, Square kingLocation) {
        this.cause = move;
        this.kingLocation = kingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KingChecked that = (KingChecked) o;
        return Objects.equals(cause, that.cause) &&
            Objects.equals(kingLocation, that.kingLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cause, kingLocation);
    }

    @Override
    public String toString() {
        return cause + "+";
    }

}
