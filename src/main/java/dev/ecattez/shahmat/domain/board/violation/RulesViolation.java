package dev.ecattez.shahmat.domain.board.violation;

public class RulesViolation extends RuntimeException {

    public RulesViolation(String message) {
        super(message);
    }

}
