package dev.ecattez.shahmat.domain.command;

import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

public class Move {

    public final PieceType type;
    public final Square from;
    public final Square to;

    public Move(PieceType type, Square from, Square to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }

}
