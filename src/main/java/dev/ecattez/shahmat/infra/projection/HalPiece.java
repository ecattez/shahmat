package dev.ecattez.shahmat.infra.projection;

import java.util.List;
import java.util.Objects;

public class HalPiece {

    private String type;
    private String color;
    private String unicode;
    private List<String> availableMoves;
    private boolean promoting;

    public HalPiece(String type, String color, String unicode, List<String> availableMoves, boolean promoting) {
        this.type = type;
        this.color = color;
        this.unicode = unicode;
        this.availableMoves = availableMoves;
        this.promoting = promoting;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getUnicode() {
        return unicode;
    }

    public List<String> getAvailableMoves() {
        return availableMoves;
    }

    public boolean isPromoting() {
        return promoting;
    }

    public boolean canMove() {
        return !availableMoves.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HalPiece halPiece = (HalPiece) o;
        return promoting == halPiece.promoting &&
            Objects.equals(type, halPiece.type) &&
            Objects.equals(color, halPiece.color) &&
            Objects.equals(unicode, halPiece.unicode) &&
            Objects.equals(availableMoves, halPiece.availableMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, color, unicode, availableMoves, promoting);
    }
}
