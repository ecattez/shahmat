package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;

public class Promote {

    public final Square location;
    public final PieceType typeOfPromotion;

    public Promote(Square location, PieceType typeOfPromotion) {
        this.location = location;
        this.typeOfPromotion = typeOfPromotion;
    }

    @Override
    public String toString() {
        return "Promote{" +
            "location=" + location +
            ", typeOfPromotion=" + typeOfPromotion +
            '}';
    }
}
