package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.event.PromotionProposed;
import dev.ecattez.shahmat.board.pawn.PawnPromotionRankVisitor;
import dev.ecattez.shahmat.command.MovePiece;
import dev.ecattez.shahmat.command.PromotePawn;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PawnPromoted;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChessGame {

    private static final PieceFactory PIECE_FACTORY = new PieceBox(); // fixme: change it

    private static final PieceTypeVisitor<MovingStrategy> MOVING_STRATEGIES = new MovingRules();

    private static final PieceColorVisitor<Integer> PROMOTION_RANKS = new PawnPromotionRankVisitor();

    public static List<BoardEvent> move(
        List<BoardEvent> history,
        MovePiece command
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
            .getPiece(from)
            .filter(command.piece::equals)
            .orElseThrow(() -> new WrongPieceSelected(from));

        if (!canBeMoved(pieceToMove, from, to, board)) {
            // todo: can be improved in the future with a cause
            throw new ImpossibleToMove(pieceToMove, from, to);
        }

        List<BoardEvent> events = new LinkedList<>();
        events.add(
            new PieceMoved(
                pieceToMove,
                from,
                to
            )
        );

        board.getPiece(to)
            .map(capturedPiece ->
                new PieceCaptured(
                    capturedPiece,
                    pieceToMove
                )
            ).ifPresent(events::add);

        if (canBePromoted(pieceToMove, to)) {
            events.add(
                new PromotionProposed(to)
            );
        }

        return events;
    }

    public static List<BoardEvent> promote(
        List<BoardEvent> history,
        PromotePawn command
    ) throws RulesViolation {
        PieceType typeOfPromotion = command.typeOfPromotion;

        if (isTryingToPromoteForAKing(typeOfPromotion)) {
            throw new PromotionRefused(PromotionRefused.Reason.PROMOTE_FOR_A_KING);
        }

        Square location = command.location;
        Piece pieceToBePromoted = replay(history)
            .getPiece(location)
            .orElseThrow(() -> new NoPieceOnSquare(location));

        if (!canBePromoted(pieceToBePromoted, location)) {
            throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_BE_PROMOTED);
        }

        return Collections.singletonList(
            new PawnPromoted(
                location,
                PIECE_FACTORY.createPiece(typeOfPromotion, pieceToBePromoted.color)
            )
        );
    }

    private static Board replay(List<BoardEvent> history) {
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

    private static boolean canBeMoved(Piece pieceOnBoard, Square from, Square to, Board board) {
        return pieceOnBoard.type.accept(MOVING_STRATEGIES)
            .canMove(pieceOnBoard, from, to, board);
    }

    private static boolean isTryingToPromoteForAKing(PieceType typeOfPromotion) {
        return PieceType.KING.equals(typeOfPromotion);
    }

    private static boolean canBePromoted(Piece pieceOnBoard, Square location) {
        return pieceOnBoard.isOfType(PieceType.PAWN)
            && pieceOnBoard.color.accept(PROMOTION_RANKS)
                .equals(location.rank.value);
    }

}
