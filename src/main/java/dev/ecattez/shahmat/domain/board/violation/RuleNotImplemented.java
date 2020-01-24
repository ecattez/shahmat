package dev.ecattez.shahmat.domain.board.violation;

public class RuleNotImplemented extends UnsupportedOperationException {

    public RuleNotImplemented(String message) {
        super(message);
    }

}
