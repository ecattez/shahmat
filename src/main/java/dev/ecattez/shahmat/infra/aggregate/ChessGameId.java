package dev.ecattez.shahmat.infra.aggregate;

import java.util.Objects;

public class ChessGameId {

    public final String value;

    private ChessGameId(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGameId that = (ChessGameId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

    public static ChessGameId newInstance() {
        return new ChessGameId(
            Long.toHexString(Double.doubleToLongBits(Math.random()))
        );
    }

    public static ChessGameId fromString(String value) {
        return new ChessGameId(value);
    }

}
