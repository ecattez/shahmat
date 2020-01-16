package dev.ecattez.shahmat.board.knight;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    @Override
    public String toString() {
        return "N";
    }
}
