package dev.ecattez.shahmat.domain.game;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.piece.move.AwareOfCheckMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.piece.move.MovingRules;
import dev.ecattez.shahmat.domain.board.piece.move.MovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnPromotionAllowedPieces;
import dev.ecattez.shahmat.domain.board.piece.pawn.PawnPromotionRankVisitor;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.PieceCanNotBeMoved;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.ChessMoveEvent;
import dev.ecattez.shahmat.domain.event.KingChecked;
import dev.ecattez.shahmat.domain.event.MovementToEventVisitor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BoardDecision {

    private static final MovementToEventVisitor EVENTS_FROM_MOVEMENT = new MovementToEventVisitor();

    public static Board replay(List<ChessEvent> history) {
        Board board = new Board();
        for (ChessEvent past: history) {
            board.apply(past);
        }
        return board;
    };

    public static Optional<Movement> findMovementAwareOfCheck(Board board, Square from, Square to, Piece pieceOnBoard) {
        return new AwareOfCheckMovingStrategy(
            getMovingStrategy(pieceOnBoard)
        ).findMovement(board, pieceOnBoard, from, to);
    }

    public static Optional<Movement> findMovement(Board board, Square from, Square to, Piece pieceOnBoard) {
        return getMovingStrategy(pieceOnBoard)
            .findMovement(board, pieceOnBoard, from, to);
    }

    public static List<Movement> getMovementsAwareOfCheck(Board board, Square from, Piece pieceOnBoard) {
        return new AwareOfCheckMovingStrategy(
            getMovingStrategy(pieceOnBoard)
        ).getAvailableMovements(board, pieceOnBoard, from);
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

    public static Optional<Square> evaluateCheck(Board board, ChessMoveEvent event) {
        Board futureBoard = new Board(board);
        futureBoard.apply(event);

        PieceColor kingColor = event.piece.color().opposite();

        return futureBoard
            .findLocationOfKing(kingColor)
            .filter(kingLocation ->
                findMovementAwareOfCheck(
                    futureBoard,
                    event.to,
                    kingLocation,
                    event.piece
                ).isPresent()
            );
    }

    public static boolean willBeChecked(Board board, ChessMoveEvent event) {
        Board futureBoard = new Board(board);
        futureBoard.apply(event);

        Piece piece = event.piece;

        return futureBoard
            .findLocationOfKing(piece.color())
            .map(kingLocation -> futureBoard
                .getOpponentsOf(piece)
                .entrySet()
                .stream()
                .anyMatch(entry ->
                    findMovement(
                        futureBoard,
                        entry.getKey(),
                        kingLocation,
                        entry.getValue()
                    ).isPresent()
                )
            )
            .orElse(Boolean.FALSE);
    }

    public static boolean wouldBePossible(Board board, Movement move) {
        ChessMoveEvent event = move.accept(EVENTS_FROM_MOVEMENT);
        return !willBeChecked(board, event);
    }

    public static List<ChessEvent> move(Board board, Square from, Square to, Piece pieceToMove) throws RulesViolation {
        List<ChessEvent> events = BoardDecision.findMovementAwareOfCheck(board, from, to, pieceToMove)
            .stream()
            .map(move -> move.accept(EVENTS_FROM_MOVEMENT))
            .map(event -> evaluateCheck(board, event)
                .<ChessEvent>map(kingLocation -> new KingChecked(event, kingLocation))
                .orElse(event))
            .collect(Collectors.toList());

        if (events.isEmpty()) {
            throw new PieceCanNotBeMoved(pieceToMove, from, to);
        }

        return events;
    }

    private static MovingStrategy getMovingStrategy(Piece piece) {
        return piece.accept(MovingRules.getInstance());
    }

    private BoardDecision() {}

}
