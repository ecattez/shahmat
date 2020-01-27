package dev.ecattez.shahmat.domain.board.piece;

public interface PieceColorVisitor<T> {

    T visitBlack();

    T visitWhite();

}
