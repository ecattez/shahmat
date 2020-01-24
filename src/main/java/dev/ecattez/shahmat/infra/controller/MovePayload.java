package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.domain.board.square.Square;

import javax.validation.constraints.Pattern;

public class MovePayload {

    private String type;
    private String to;

    // todo: should be a good PieceType
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Pattern(regexp = Square.SQUARE_PATTERN)
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
