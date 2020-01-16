package dev.ecattez.shahmat.board.bishop;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;

public class Bishop extends Piece {

    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    @Override
    public String toString() {
        return "B";
    }
}
