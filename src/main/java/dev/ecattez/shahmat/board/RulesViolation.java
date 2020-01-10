package dev.ecattez.shahmat.board;

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
