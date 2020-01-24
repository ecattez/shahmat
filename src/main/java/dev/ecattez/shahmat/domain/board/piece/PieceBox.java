package dev.ecattez.shahmat.domain.board.piece;

import dev.ecattez.shahmat.domain.board.piece.bishop.Bishop;
import dev.ecattez.shahmat.domain.board.piece.king.King;
import dev.ecattez.shahmat.domain.board.piece.knight.Knight;
import dev.ecattez.shahmat.domain.board.piece.pawn.Pawn;
import dev.ecattez.shahmat.domain.board.piece.queen.Queen;
import dev.ecattez.shahmat.domain.board.piece.rook.Rook;

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
