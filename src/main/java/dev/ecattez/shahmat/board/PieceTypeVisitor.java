package dev.ecattez.shahmat.board;

public interface PieceTypeVisitor<T> {

    T visitPawn();

    T visitRook();

    T visitBishop();

    T visitKnight();

    T visitQueen();

    T visitKing();

}
