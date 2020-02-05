package dev.ecattez.shahmat.domain.board.piece.knight;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.DirectionVisitor;

public class PerpendicularDirectionVisitor implements DirectionVisitor<Direction[]> {

    @Override
    public Direction[] visitForward() {
        return new Direction[]{Direction.SHIFT_LEFT, Direction.SHIFT_RIGHT};
    }

    @Override
    public Direction[] visitForwardLeft() {
        return new Direction[]{Direction.FORWARD, Direction.SHIFT_LEFT};
    }

    @Override
    public Direction[] visitForwardRight() {
        return new Direction[]{Direction.FORWARD, Direction.SHIFT_RIGHT};
    }

    @Override
    public Direction[] visitShiftLeft() {
        return new Direction[]{Direction.FORWARD, Direction.BACKWARD};
    }

    @Override
    public Direction[] visitShiftRight() {
        return new Direction[]{Direction.FORWARD, Direction.BACKWARD};
    }

    @Override
    public Direction[] visitBackward() {
        return new Direction[]{Direction.SHIFT_LEFT, Direction.SHIFT_RIGHT};
    }

    @Override
    public Direction[] visitBackwardLeft() {
        return new Direction[]{Direction.BACKWARD, Direction.SHIFT_LEFT};
    }

    @Override
    public Direction[] visitBackwardRight() {
        return new Direction[]{Direction.BACKWARD, Direction.SHIFT_RIGHT};
    }

}
