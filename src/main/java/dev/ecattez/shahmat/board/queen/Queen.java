package dev.ecattez.shahmat.board.queen;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;

public class Queen extends Piece {

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    @Override
    public String toString() {
        return "Q";
    }
}
