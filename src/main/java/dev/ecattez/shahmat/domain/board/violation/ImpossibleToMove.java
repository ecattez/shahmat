package dev.ecattez.shahmat.domain.board.violation;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

public class ImpossibleToMove extends RulesViolation {

    public ImpossibleToMove(Piece piece, Square from, Square to) {
        super("Piece " + piece + " can not be moved from " + from + " to " + to);
    }

}
