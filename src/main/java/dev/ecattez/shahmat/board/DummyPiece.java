package dev.ecattez.shahmat.board;

@Deprecated
public class DummyPiece extends Piece {

    public DummyPiece(PieceType type, PieceColor color) {
        super(type, color);
    }

    @Override
    public String toString() {
        return "X";
    }
}
