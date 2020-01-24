package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.piece.PieceTypeVisitor;

public class PawnPromotionAllowedPieces implements PieceTypeVisitor<Boolean> {

    private static PawnPromotionAllowedPieces instance;

    public static PawnPromotionAllowedPieces getInstance() {
        if (instance == null) {
            instance = new PawnPromotionAllowedPieces();
        }
        return instance;
    }

    @Override
    public Boolean visitPawn() {
        return false;
    }

    @Override
    public Boolean visitRook() {
        return true;
    }

    @Override
    public Boolean visitBishop() {
        return true;
    }

    @Override
    public Boolean visitKnight() {
        return true;
    }

    @Override
    public Boolean visitQueen() {
        return true;
    }

    @Override
    public Boolean visitKing() {
        return false;
    }

}
