package dev.ecattez.shahmat.domain.game;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.piece.move.MovingRules;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnPromotionAllowedPieces;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnPromotionRankVisitor;
import dev.ecattez.shahmat.domain.event.ChessEvent;

import java.util.List;
import java.util.Optional;

public class BoardDecision {

    public static Board replay(List<ChessEvent> history) {
        Board board = new Board();
        for (ChessEvent past: history) {
            board.apply(past);
        }
        return board;
    };

    public static Optional<Movement> findMovement(Board board, Square from, Square to, Piece pieceOnBoard) {
        return pieceOnBoard.type().accept(MovingRules.getInstance())
            .findMovement(board, pieceOnBoard, from, to);
    }

    public static List<Movement> getMovements(Board board, Square from, Piece pieceOnBoard) {
        return pieceOnBoard.type().accept(MovingRules.getInstance())
            .getAvailableMovements(board, pieceOnBoard, from);
    }

    public static boolean canPromote(PieceType typeOfPromotion) {
        return typeOfPromotion.accept(PawnPromotionAllowedPieces.getInstance());
    }

    public static boolean canBePromoted(Square location, Piece pieceOnBoard) {
        return pieceOnBoard.isOfType(PieceType.PAWN) &&
            pieceOnBoard
                .color()
                .accept(PawnPromotionRankVisitor.getInstance())
                .equals(location.rank);
    }

    public static PieceColor whoseNextTurnIs(Board board) {
        return board
            .turnOf()
            .opposite();
    }

    public static boolean isOwnedByCurrentPlayer(Board board, Piece piece) {
        return board.isTurnOf(piece.color());
    }

}
