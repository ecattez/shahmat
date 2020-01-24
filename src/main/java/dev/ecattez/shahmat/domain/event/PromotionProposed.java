package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Objects;

public class PromotionProposed implements ChessEvent {

    public final Square location;

    public PromotionProposed(Square location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionProposed that = (PromotionProposed) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    @Override
    public String toString() {
        return "PromotionProposed{" +
            "location=" + location +
            '}';
    }

}
