package dev.ecattez.shahmat.domain.board.piece;

public interface Typed {

    boolean isOfType(PieceType type);

    PieceType type();

}
