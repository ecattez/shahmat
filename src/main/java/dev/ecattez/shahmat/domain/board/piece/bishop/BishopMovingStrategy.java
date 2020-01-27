package dev.ecattez.shahmat.domain.board.piece.bishop;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.move.StopOnObstructionMovingStrategy;

public class BishopMovingStrategy extends StopOnObstructionMovingStrategy {

    public static final Direction[] MOVING_DIRECTIONS = new Direction[]{
        Direction.FORWARD_LEFT,
        Direction.FORWARD_RIGHT,
        Direction.BACKWARD_LEFT,
        Direction.BACKWARD_RIGHT
    };

    public BishopMovingStrategy() {
        super(MOVING_DIRECTIONS);
    }

}
