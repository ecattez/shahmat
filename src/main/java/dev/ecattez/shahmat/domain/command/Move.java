package dev.ecattez.shahmat.domain.command;

import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

import javax.annotation.Nullable;

public class Move {

    public final PieceType type;
    public final Square from;
    public final Square to;
    public final PieceType promotedTo;

    public Move(PieceType type, Square from, Square to, @Nullable PieceType promotedTo) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.promotedTo = promotedTo;
    }

    public Move(PieceType type, Square from, Square to) {
        this(type, from, to, null);
    }

}
