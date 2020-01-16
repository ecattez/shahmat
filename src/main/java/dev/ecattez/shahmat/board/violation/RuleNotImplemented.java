package dev.ecattez.shahmat.board.violation;

public class RuleNotImplemented extends UnsupportedOperationException {

    public RuleNotImplemented() {}

    public RuleNotImplemented(String message) {
        super(message);
    }

}
