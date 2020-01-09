package dev.ecattez.shahmat.board;

public class TradeRefused extends RuntimeException {

    public TradeRefused(Square location) {
        super("Pawn in " + location + "can not be traded for a King");
    }

}
