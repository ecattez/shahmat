package dev.ecattez.shahmat.command;

import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;

public class TradePawn {

    public final Square location;
    public final PieceType typeOfTrade;

    public TradePawn(Square location, PieceType typeOfTrade) {
        this.location = location;
        this.typeOfTrade = typeOfTrade;
    }
}
