package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;

public class PromotePawn {

    public final Square location;
    public final PieceType typeOfPromotion;

    public PromotePawn(Square location, PieceType typeOfPromotion) {
        this.location = location;
        this.typeOfPromotion = typeOfPromotion;
    }
}
