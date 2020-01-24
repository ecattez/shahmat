package dev.ecattez.shahmat.domain.command;

import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;

public class Promote {

    public final Square location;
    public final PieceType typeOfPromotion;

    public Promote(Square location, PieceType typeOfPromotion) {
        this.location = location;
        this.typeOfPromotion = typeOfPromotion;
    }

}
