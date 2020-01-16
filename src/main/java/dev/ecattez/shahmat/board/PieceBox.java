package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.bishop.Bishop;
import dev.ecattez.shahmat.board.king.King;
import dev.ecattez.shahmat.board.knight.Knight;
import dev.ecattez.shahmat.board.pawn.Pawn;
import dev.ecattez.shahmat.board.queen.Queen;
import dev.ecattez.shahmat.board.rook.Rook;

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
                return new Rook(color);
            }

            @Override
            public Piece visitBishop() {
                return new Bishop(color);
            }

            @Override
            public Piece visitKnight() {
                return new Knight(color);
            }

            @Override
            public Piece visitQueen() {
                return new Queen(color);
            }

            @Override
            public Piece visitKing() {
                return new King(color);
            }
        });
    }

}
