package dev.ecattez.shahmat.board;

public class TradeRefused extends RulesViolation {

    public TradeRefused(Reason reason) {
        super(reason.message);
    }

    public enum Reason {
        TRADE_FOR_A_KING("Trade is not allowed with a King"),
        PIECE_IS_NOT_TRADABLE("Piece is not tradable");

        public final String message;

        Reason(String message) {
            this.message = message;
        }
    }

}
