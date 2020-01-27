package dev.ecattez.shahmat.domain.board.piece;

public interface PieceTypeVisitor<T> {

    T visitPawn();

    T visitRook();

    T visitBishop();

    T visitKnight();

    T visitQueen();

    T visitKing();

}
