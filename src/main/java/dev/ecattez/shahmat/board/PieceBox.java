package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.pawn.Pawn;

public class PieceBox implements PieceFactory {

    @Override
    public Piece createPiece(PieceType type, PieceColor color) {
        return type.accept(new PieceTypeVisitor<Piece>() {
            @Override
            public Piece visitPawn() {
                return new Pawn(color);
            }

            @Override
            public Piece visitRook() {
                return null;
            }

            @Override
            public Piece visitBishop() {
                return null;
            }

            @Override
            public Piece visitKnight() {
                return null;
            }

            @Override
            public Piece visitQueen() {
                return null;
            }

            @Override
            public Piece visitKing() {
                return null;
            }
        });
    }

}
