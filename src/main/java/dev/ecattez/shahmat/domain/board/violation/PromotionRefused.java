package dev.ecattez.shahmat.domain.board.violation;

public class PromotionRefused extends RulesViolation {

    public PromotionRefused(Reason reason) {
        super(reason.message);
    }

    public enum Reason {
        PIECE_CAN_NOT_PROMOTE("Piece can not promote"),
        PIECE_CAN_NOT_BE_PROMOTED("Piece can not be promoted");

        public final String message;

        Reason(String message) {
            this.message = message;
        }
    }

}
