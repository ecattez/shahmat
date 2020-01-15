package dev.ecattez.shahmat.board.move;

import dev.ecattez.shahmat.board.PieceTypeVisitor;
import dev.ecattez.shahmat.board.move.MovingStrategy;
import dev.ecattez.shahmat.board.pawn.PawnMovingStrategy;
import dev.ecattez.shahmat.board.violation.RuleNotImplemented;

public class MovingRules implements PieceTypeVisitor<MovingStrategy> {

    @Override
    public MovingStrategy visitPawn() {
        return new PawnMovingStrategy();
    }

    @Override
    public MovingStrategy visitRook() {
        throw new RuleNotImplemented();
    }

    @Override
    public MovingStrategy visitBishop() {
        throw new RuleNotImplemented();
    }

    @Override
    public MovingStrategy visitKnight() {
        throw new RuleNotImplemented();
    }

    @Override
    public MovingStrategy visitQueen() {
        throw new RuleNotImplemented();
    }

    @Override
    public MovingStrategy visitKing() {
        throw new RuleNotImplemented();
    }

}
