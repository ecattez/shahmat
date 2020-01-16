package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    @Override
    public String toString() {
        return "P";
    }
}
