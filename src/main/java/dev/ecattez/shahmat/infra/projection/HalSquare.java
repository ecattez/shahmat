package dev.ecattez.shahmat.infra.projection;

import java.util.Objects;

public class HalSquare {

    private String location;
    private HalPiece piece;

    public HalSquare(String location, HalPiece piece) {
        this.piece = piece;
        this.location = location;
    }

    public boolean isVacant() {
        return piece == null;
    }

    public HalPiece getPiece() {
        return piece;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HalSquare halSquare = (HalSquare) o;
        return Objects.equals(location, halSquare.location) &&
            Objects.equals(piece, halSquare.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location, piece);
    }
}
