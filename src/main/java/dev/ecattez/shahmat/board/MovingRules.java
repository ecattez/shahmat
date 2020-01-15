package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.move.MovingStrategy;
import dev.ecattez.shahmat.board.pawn.PawnMovingStrategy;

public class MovingRules implements PieceTypeVisitor<MovingStrategy> {

    @Override
    public MovingStrategy visitPawn() {
        return new PawnMovingStrategy();
    }

    @Override
    public MovingStrategy visitRook() {
        return null;
    }

    @Override
    public MovingStrategy visitBishop() {
        return null;
    }

    @Override
    public MovingStrategy visitKnight() {
        return null;
    }

    @Override
    public MovingStrategy visitQueen() {
        return null;
    }

    @Override
    public MovingStrategy visitKing() {
        return null;
    }

}
