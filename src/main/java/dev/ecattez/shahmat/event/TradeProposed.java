package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Square;

import java.util.Objects;

public class TradeProposed implements BoardEvent {

    public final Square location;

    public TradeProposed(Square location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeProposed that = (TradeProposed) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "TradeProposed{" +
            "location=" + location +
            '}';
    }

}
