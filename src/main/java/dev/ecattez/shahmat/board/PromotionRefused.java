package dev.ecattez.shahmat.board;

public class PromotionRefused extends RulesViolation {

    public PromotionRefused(Reason reason) {
        super(reason.message);
    }

    public enum Reason {
        PROMOTE_FOR_A_KING("Promotion is not allowed with a King"),
        PIECE_CAN_NOT_BE_PROMOTED("Piece can not be promoted");

        public final String message;

        Reason(String message) {
            this.message = message;
        }
    }

}
