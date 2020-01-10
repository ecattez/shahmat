package dev.ecattez.shahmat.board;

public class ImpossibleToMove extends RulesViolation {

    public ImpossibleToMove(Piece piece, Square from, Square to) {
        super("Piece " + piece + " can not be moved from " + from + " to " + to);
    }

}
