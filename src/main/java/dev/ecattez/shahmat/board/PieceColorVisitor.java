package dev.ecattez.shahmat.board;

public interface PieceColorVisitor<T> {

    T visitBlack();

    T visitWhite();

}
