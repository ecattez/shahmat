package dev.ecattez.shahmat.domain.game;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceBox;
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
import dev.ecattez.shahmat.domain.board.violation.PromotionMustBeDone;
import dev.ecattez.shahmat.domain.board.violation.PromotionRefused;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.ChessMoveEvent;
import dev.ecattez.shahmat.domain.event.KingChecked;
import dev.ecattez.shahmat.domain.event.KingCheckmated;
import dev.ecattez.shahmat.domain.event.MovementToEventVisitor;
import dev.ecattez.shahmat.domain.event.PawnPromoted;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardDecision {

    private static final MovementToEventVisitor EVENTS_FROM_MOVEMENT = new MovementToEventVisitor();

    public static Board replay(List<ChessEvent> history) {
        Board board = new Board();
        for (ChessEvent past: history) {
            board.apply(past);
        }
        return board;
    };

    public static Board replay(List<ChessEvent> history, List<ChessEvent> newEvents) {
        return replay(
            Stream.of(
                history,
                newEvents
            )
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );
    }

    public static Board replay(List<ChessEvent> history, ChessEvent... newEvents) {
        return replay(history, List.of(newEvents));
    }

    public static boolean canMoveAwareOfCheck(Board board, Square from, Piece pieceOnBoard) {
        return !new AwareOfCheckMovingStrategy(
            getMovingStrategy(pieceOnBoard)
        ).getAvailableMovements(board, pieceOnBoard, from).isEmpty();
    }

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

    public static Optional<Square> evaluateCheckmate(Board board, ChessMoveEvent event) {
        PieceColor kingColor = event.piece.color().opposite();

        return evaluateCheck(board, event)
            .filter(kingIsCheckmated(board, event, kingColor));
    }

    private static Predicate<Square> kingIsCheckmated(Board board, ChessEvent event, PieceColor kingColor) {
        return kingLocation -> {
            Board futureBoard = new Board(board);
            futureBoard.apply(event);

            boolean canBeProtected = futureBoard
                .getPieces(kingColor)
                .entrySet()
                .stream()
                .map(entry -> canMoveAwareOfCheck(futureBoard, entry.getKey(), entry.getValue()))
                .reduce((acc, current) -> acc || current)
                .orElse(Boolean.FALSE);

            return !canBeProtected;
        };
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

    public static ChessMoveEvent evaluatePromotion(
        ChessMoveEvent event,
        @Nullable PieceType promotedTo
    ) {
        if (promotedTo == null) {
            if (canBePromoted(event.to, event.piece)) {
                throw new PromotionMustBeDone(event.to);
            }
        } else {
            if (!canBePromoted(event.to, event.piece)) {
                throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_BE_PROMOTED);
            }
            if (!canPromote(promotedTo)) {
                throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_PROMOTE);
            }
            return new PawnPromoted(
                event,
                PieceBox.getInstance().createPiece(promotedTo, event.piece.color())
            );
        }
        return event;
    }

    public static ChessEvent move(
        Board board,
        Square from,
        Square to,
        Piece pieceToMove,
        @Nullable PieceType promotedTo
    ) throws RulesViolation {
        return BoardDecision.findMovementAwareOfCheck(board, from, to, pieceToMove)
            .map(move -> move.accept(EVENTS_FROM_MOVEMENT))
            .map(event -> evaluatePromotion(event, promotedTo))
            .map(event -> evaluateCheckmate(board, event)
                .<ChessEvent>map(kingLocation -> new KingCheckmated(event, kingLocation))
                .orElseGet(() -> evaluateCheck(board, event)
                    .<ChessEvent>map(kingLocation -> new KingChecked(event, kingLocation))
                    .orElse(event)))
            .orElseThrow(() -> new PieceCanNotBeMoved(pieceToMove, from, to));
    }

    private static MovingStrategy getMovingStrategy(Piece piece) {
        return piece.accept(MovingRules.getInstance());
    }

    private BoardDecision() {}

}
