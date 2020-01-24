package dev.ecattez.shahmat.domain.board.piece;

import dev.ecattez.shahmat.domain.board.Orientation;

import java.util.Objects;

public abstract class Piece implements Figurine, Typed, Colored, Oriented {

    private static final PieceColorVisitor<Orientation> COLOR_ORIENTED = new PieceColorOrientationVisitor();

    private final PieceType type;
    private final PieceColor color;
    private final Orientation orientation;
    private final String unicode;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
        this.orientation = color.accept(COLOR_ORIENTED);
        this.unicode = PieceUnicodeConverter.convert(this);
    }

    @Override
    public boolean isOfType(PieceType type) {
        return this.type.equals(type);
    }

    @Override
    public PieceType type() {
        return type;
    }

    @Override
    public boolean isOfColor(PieceColor color) {
        return this.color.equals(color);
    }

    @Override
    public PieceColor color() {
        return color;
    }

    @Override
    public Orientation orientation() {
        return orientation;
    }

    @Override
    public String unicode() {
        return unicode;
    }

    public boolean isOpponent(Piece piece) {
        return piece != null && piece.isOfColor(this.color.opposite());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type &&
            color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return unicode();
    }

}
