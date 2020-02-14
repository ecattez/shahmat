package dev.ecattez.shahmat.domain.board.violation;

import dev.ecattez.shahmat.domain.board.square.Square;

public class PromotionMustBeDone extends RulesViolation {

    public PromotionMustBeDone(Square location) {
        super("Pawn can not move to " + location + " without being promoted");
    }

}
