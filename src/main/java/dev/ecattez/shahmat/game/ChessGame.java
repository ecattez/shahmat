package dev.ecattez.shahmat.game;

import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceBox;
import dev.ecattez.shahmat.board.PieceColorVisitor;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.PieceTypeVisitor;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.move.Movement;
import dev.ecattez.shahmat.board.move.MovingRules;
import dev.ecattez.shahmat.board.move.MovingStrategy;
import dev.ecattez.shahmat.board.pawn.PawnPromotionAllowedPieces;
import dev.ecattez.shahmat.board.violation.ImpossibleToMove;
import dev.ecattez.shahmat.board.violation.InvalidMove;
import dev.ecattez.shahmat.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.board.violation.PromotionRefused;
import dev.ecattez.shahmat.board.violation.RulesViolation;
import dev.ecattez.shahmat.board.violation.WrongPieceSelected;
import dev.ecattez.shahmat.event.MovementToEventVisitor;
import dev.ecattez.shahmat.event.PromotionProposed;
import dev.ecattez.shahmat.board.pawn.PawnPromotionRankVisitor;
import dev.ecattez.shahmat.command.Move;
import dev.ecattez.shahmat.command.Promote;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PawnPromoted;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessGame {

    private static final PieceTypeVisitor<MovingStrategy> MOVING_RULES = new MovingRules();

    private static final PieceTypeVisitor<Boolean> PIECES_THAT_CAN_PROMOTE = new PawnPromotionAllowedPieces();

    private static final PieceColorVisitor<Square.Rank> PROMOTION_RANKS = new PawnPromotionRankVisitor();

    private static final MovementToEventVisitor EVENTS_FROM_MOVEMENT = new MovementToEventVisitor();

    public static List<BoardEvent> move(
        List<BoardEvent> history,
        Move command
    ) throws RulesViolation {
        Square from = command.from;
        Square to = command.to;

        if (from.equals(to)) {
            throw new InvalidMove("Current position and destination are the same square");
        }

        Board board = replay(history);
        if (board.isVacant(from)) {
            throw new NoPieceOnSquare(from);
        }

        Piece pieceToMove = board
            .findPiece(from)
            .filter(command.piece::equals)
            .orElseThrow(() -> new WrongPieceSelected(from));

        List<BoardEvent> events = findMovement(pieceToMove, from, to, board, history)
            .stream()
            .map(move -> move.accept(EVENTS_FROM_MOVEMENT))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (events.isEmpty()) {
            throw new ImpossibleToMove(pieceToMove, from, to);
        }

        if (canBePromoted(pieceToMove, to)) {
            events.add(
                new PromotionProposed(to)
            );
        }

        return events;
    }

    public static List<BoardEvent> promote(
        List<BoardEvent> history,
        Promote command
    ) throws RulesViolation {
        PieceType typeOfPromotion = command.typeOfPromotion;

        if (!canPromote(typeOfPromotion)) {
            throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_PROMOTE);
        }

        Square location = command.location;
        Piece pieceToBePromoted = replay(history)
            .findPiece(location)
            .orElseThrow(() -> new NoPieceOnSquare(location));

        if (!canBePromoted(pieceToBePromoted, location)) {
            throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_BE_PROMOTED);
        }

        return Collections.singletonList(
            new PawnPromoted(
                location,
                PieceBox.getInstance().createPiece(typeOfPromotion, pieceToBePromoted.color)
            )
        );
    }

    public static Board replay(List<BoardEvent> history) {
        Board board = new Board();
        for (BoardEvent past: history) {
            // todo: visitor pattern + appliers ?
            if (past instanceof PiecePositioned) {
                board.apply((PiecePositioned) past);
            } else if (past instanceof PieceMoved) {
                board.apply((PieceMoved) past);
            } else if (past instanceof PieceCaptured) {
                board.apply((PieceCaptured) past);
            } else if (past instanceof PromotionProposed) {
                board.apply((PromotionProposed) past);
            } else if (past instanceof PawnPromoted) {
                board.apply((PawnPromoted) past);
            }
        }
        return board;
    };

    private static Optional<Movement> findMovement(Piece pieceOnBoard, Square from, Square to, Board board, List<BoardEvent> history) {
        return pieceOnBoard.type.accept(MOVING_RULES)
            .findMovement(history, board, pieceOnBoard, from, to);
    }

    private static boolean canPromote(PieceType typeOfPromotion) {
        return typeOfPromotion.accept(PIECES_THAT_CAN_PROMOTE);
    }

    private static boolean canBePromoted(Piece pieceOnBoard, Square location) {
        return pieceOnBoard.isOfType(PieceType.PAWN)
            && pieceOnBoard.color.accept(PROMOTION_RANKS)
                .equals(location.rank);
    }

}
