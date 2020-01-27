package dev.ecattez.shahmat.domain.board.piece;

public interface PieceFactory {

    Piece createPiece(PieceType type, PieceColor color);

}
