package dev.ecattez.shahmat.board.rook;

import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.move.StopOnObstructionMovingStrategy;

public class RookMovingStrategy extends StopOnObstructionMovingStrategy {

    private static final Direction[] MOVING_DIRECTIONS = new Direction[]{
        Direction.FORWARD,
        Direction.BACKWARD,
        Direction.SHIFT_LEFT,
        Direction.SHIFT_RIGHT
    };

    public RookMovingStrategy() {
        super(MOVING_DIRECTIONS);
    }

}
