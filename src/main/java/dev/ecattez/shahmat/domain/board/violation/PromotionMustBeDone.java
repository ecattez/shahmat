package dev.ecattez.shahmat.domain.board.violation;

public class PromotionMustBeDone extends RulesViolation {

    public PromotionMustBeDone() {
        super("Promotion must be done before any other command");
    }

}
