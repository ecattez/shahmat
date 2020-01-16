package dev.ecattez.shahmat.board.king;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;

public class King extends Piece {

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    @Override
    public String toString() {
        return "K";
    }
}
