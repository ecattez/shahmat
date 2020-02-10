package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class KingChecked implements ChessEvent {

    public final ChessEvent event;
    public final Square kingLocation;

    public KingChecked(ChessEvent event, Square kingLocation) {
        this.event = event;
        this.kingLocation = kingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KingChecked that = (KingChecked) o;
        return Objects.equals(event, that.event) &&
            Objects.equals(kingLocation, that.kingLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, kingLocation);
    }

    @Override
    public String toString() {
        return event.toString() + "+";
    }

}
