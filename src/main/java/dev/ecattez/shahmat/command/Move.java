package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

public class Move {

    public final Piece piece;
    public final Square from;
    public final Square to;

    public Move(Piece piece, Square from, Square to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

}
