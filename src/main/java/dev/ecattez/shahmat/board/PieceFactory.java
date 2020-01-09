package dev.ecattez.shahmat.board;

public interface PieceFactory {

    Piece createPiece(PieceType type, PieceColor color);

}
