package dev.ecattez.shahmat.board.queen;

import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.move.StopOnObstructionMovingStrategy;

public class QueenMovingStrategy extends StopOnObstructionMovingStrategy {

    public static final Direction[] MOVING_DIRECTIONS = Direction.values();

    public QueenMovingStrategy() {
        super(MOVING_DIRECTIONS);
    }

}
