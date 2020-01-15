package dev.ecattez.shahmat.board.violation;

public class RulesViolation extends RuntimeException {

    public RulesViolation() {
        super();
    }

    public RulesViolation(String message) {
        super(message);
    }

    public RulesViolation(String message, Throwable cause) {
        super(message, cause);
    }

}
