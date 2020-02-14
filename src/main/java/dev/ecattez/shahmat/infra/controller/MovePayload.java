package dev.ecattez.shahmat.infra.controller;

import dev.ecattez.shahmat.domain.board.square.Square;

import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;

public class MovePayload {

    // todo: should be a valid PieceType
    private String type;
    private String to;
    // todo: should be null or a valid PieceType
    @Nullable
    private String promotedTo;

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

    public String getPromotedTo() {
        return promotedTo;
    }

    public void setPromotedTo(String promotedTo) {
        this.promotedTo = promotedTo;
    }
}
