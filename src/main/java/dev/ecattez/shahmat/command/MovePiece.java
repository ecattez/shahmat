package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

public class MovePiece {

    public final Piece piece;
    public final Square from;
    public final Square to;

    public MovePiece(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

}
