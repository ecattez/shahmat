package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

public class PieceMoved extends ChessMoveEvent {

    public PieceMoved(Piece piece, Square from, Square to) {
        super(piece, from, to);
    }

    @Override
    public String toString() {
        if (piece.isOfType(PieceType.PAWN)) {
            return to.toString();
        }
        return String.format("%s%s", piece, to);
    }
}
