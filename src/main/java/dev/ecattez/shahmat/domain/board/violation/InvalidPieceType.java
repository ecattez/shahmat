package dev.ecattez.shahmat.domain.board.violation;

public class InvalidPieceType extends RulesViolation {

    public InvalidPieceType(String pieceType) {
        super(pieceType + " is not a valid piece type");
    }

}
