package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.piece.PieceTypeVisitor;
import dev.ecattez.shahmat.domain.board.piece.bishop.BishopMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.knight.KnightMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.king.KingMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.knight.KnightMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.queen.QueenMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.rook.RookMovingStrategy;

public class MovingRules implements PieceTypeVisitor<MovingStrategy> {

    private static MovingRules instance;

    public static MovingRules getInstance() {
        if (instance == null) {
            instance = new MovingRules();
        }
        return instance;
    }

    private MovingRules() {}

    @Override
    public MovingStrategy visitPawn() {
        return new PawnMovingStrategy();
    }

    @Override
    public MovingStrategy visitRook() {
        return new RookMovingStrategy();
    }

    @Override
    public MovingStrategy visitBishop() {
        return new BishopMovingStrategy();
    }

    @Override
    public MovingStrategy visitKnight() {
        return new KnightMovingStrategy();
    }

    @Override
    public MovingStrategy visitQueen() {
        return new QueenMovingStrategy();
    }

    @Override
    public MovingStrategy visitKing() {
        return new KingMovingStrategy();
    }

}
