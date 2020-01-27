package dev.ecattez.shahmat.infra;

import java.util.Objects;

public class MovePair {

    public final String whiteMove;
    public final String blackMove;

    public MovePair(String whiteMove, String blackMove) {
        this.whiteMove = whiteMove;
        this.blackMove = blackMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovePair movePair = (MovePair) o;
        return Objects.equals(whiteMove, movePair.whiteMove) &&
            Objects.equals(blackMove, movePair.blackMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whiteMove, blackMove);
    }
}
