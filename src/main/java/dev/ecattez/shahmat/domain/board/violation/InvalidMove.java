package dev.ecattez.shahmat.domain.board.violation;

public class InvalidMove extends RulesViolation {

    public InvalidMove(String reason) {
        super(reason);
    }

}
