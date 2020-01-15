package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.PieceTypeVisitor;

public class PawnPromotionAllowedPieces implements PieceTypeVisitor<Boolean> {

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
