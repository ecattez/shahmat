package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.pawn.Pawn;
import dev.ecattez.shahmat.board.violation.RuleNotImplemented;

public class PieceBox implements PieceFactory {

    private static PieceBox instance;

    private PieceBox() {}

    public static PieceBox getInstance() {
        if (instance == null) {
            instance = new PieceBox();
        }
        return instance;
    }

    @Override
    public Piece createPiece(PieceType type, PieceColor color) {
        return type.accept(new PieceTypeVisitor<>() {
            @Override
            public Piece visitPawn() {
                return new Pawn(color);
            }

            @Override
            public Piece visitRook() {
                return new DummyPiece(type, color);
            }

            @Override
            public Piece visitBishop() {
                return new DummyPiece(type, color);
            }

            @Override
            public Piece visitKnight() {
                return new DummyPiece(type, color);
            }

            @Override
            public Piece visitQueen() {
                return new DummyPiece(type, color);
            }

            @Override
            public Piece visitKing() {
                return new DummyPiece(type, color);
            }
        });
    }

}
