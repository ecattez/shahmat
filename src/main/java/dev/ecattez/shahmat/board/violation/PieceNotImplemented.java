package dev.ecattez.shahmat.board.violation;

import dev.ecattez.shahmat.board.PieceType;

public class PieceNotImplemented extends RuleNotImplemented {

    public PieceNotImplemented(PieceType pieceType) {
        super(pieceType + " is not implemented yet");
    }

}
