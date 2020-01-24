package dev.ecattez.shahmat.domain.board.piece.queen;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;

public class Queen extends Piece {

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

}
