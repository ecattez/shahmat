package dev.ecattez.shahmat.domain.board.piece.king;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;

public class King extends Piece {

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

}
