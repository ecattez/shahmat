package dev.ecattez.shahmat.game.init;

import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColor;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PiecePositioned;

import java.util.List;

public class ClassicalBoardInitialization implements BoardInitialization {

    @Override
    public List<BoardEvent> initialize() {
        return List.of(
            position(PieceColor.WHITE, PieceType.ROOK, "A1"),
            position(PieceColor.WHITE, PieceType.KNIGHT, "B1"),
            position(PieceColor.WHITE, PieceType.BISHOP, "C1"),
            position(PieceColor.WHITE, PieceType.QUEEN, "D1"),
            position(PieceColor.WHITE, PieceType.KING, "E1"),
            position(PieceColor.WHITE, PieceType.BISHOP, "F1"),
            position(PieceColor.WHITE, PieceType.KNIGHT, "G1"),
            position(PieceColor.WHITE, PieceType.ROOK, "H1"),
            position(PieceColor.WHITE, PieceType.PAWN, "A2"),
            position(PieceColor.WHITE, PieceType.PAWN, "B2"),
            position(PieceColor.WHITE, PieceType.PAWN, "C2"),
            position(PieceColor.WHITE, PieceType.PAWN, "D2"),
            position(PieceColor.WHITE, PieceType.PAWN, "E2"),
            position(PieceColor.WHITE, PieceType.PAWN, "F2"),
            position(PieceColor.WHITE, PieceType.PAWN, "G2"),
            position(PieceColor.WHITE, PieceType.PAWN, "H2"),

            position(PieceColor.BLACK, PieceType.ROOK, "A8"),
            position(PieceColor.BLACK, PieceType.KNIGHT, "B8"),
            position(PieceColor.BLACK, PieceType.BISHOP, "C8"),
            position(PieceColor.BLACK, PieceType.QUEEN, "D8"),
            position(PieceColor.BLACK, PieceType.KING, "E8"),
            position(PieceColor.BLACK, PieceType.BISHOP, "F8"),
            position(PieceColor.BLACK, PieceType.KNIGHT, "G8"),
            position(PieceColor.BLACK, PieceType.ROOK, "H8"),
            position(PieceColor.BLACK, PieceType.PAWN, "A7"),
            position(PieceColor.BLACK, PieceType.PAWN, "B7"),
            position(PieceColor.BLACK, PieceType.PAWN, "C7"),
            position(PieceColor.BLACK, PieceType.PAWN, "D7"),
            position(PieceColor.BLACK, PieceType.PAWN, "E7"),
            position(PieceColor.BLACK, PieceType.PAWN, "F7"),
            position(PieceColor.BLACK, PieceType.PAWN, "G7"),
            position(PieceColor.BLACK, PieceType.PAWN, "H7")
        );
    }

    private BoardEvent position(PieceColor color, PieceType type, String location) {
        return new PiecePositioned(
            PieceBox.getInstance().createPiece(type, color),
            new Square(location)
        );
    }
}
