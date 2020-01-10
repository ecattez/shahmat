package dev.ecattez.shahmat.board;

public class InvalidMove extends RulesViolation {

    public InvalidMove(String reason) {
        super(reason);
    }

}
