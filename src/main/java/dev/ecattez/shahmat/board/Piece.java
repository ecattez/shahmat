package dev.ecattez.shahmat.board;

import java.util.Objects;

public abstract class Piece implements Typed, Colored, Oriented {

    public final PieceType type;
    public final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public boolean isOfType(PieceType type) {
        return this.type.equals(type);
    }

    @Override
    public boolean isOfColor(PieceColor color) {
        return this.color.equals(color);
    }

    @Override
    public Orientation orientation() {
        return color.accept(new PieceColorVisitor<Orientation>() {
            @Override
            public Orientation visitBlack() {
                return Orientation.BLACK_VIEW;
            }

            @Override
            public Orientation visitWhite() {
                return Orientation.WHITE_VIEW;
            }
        });
    }

    public boolean isAlly(Piece piece) {
        return piece != null && piece.isOfColor(this.color);
    }

    public boolean isOpponent(Piece piece) {
        return !isAlly(piece);
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

}
