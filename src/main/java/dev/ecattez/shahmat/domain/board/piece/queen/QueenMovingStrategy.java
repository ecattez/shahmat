package dev.ecattez.shahmat.domain.board.piece.queen;

import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.piece.move.StopOnObstructionMovingStrategy;

public class QueenMovingStrategy extends StopOnObstructionMovingStrategy {

    public static final Direction[] MOVING_DIRECTIONS = Direction.values();

    public QueenMovingStrategy() {
        super(MOVING_DIRECTIONS);
    }

}
