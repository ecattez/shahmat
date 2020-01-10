package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.event.TradeProposed;
import dev.ecattez.shahmat.board.pawn.PawnTradingRankVisitor;
import dev.ecattez.shahmat.command.MovePiece;
import dev.ecattez.shahmat.command.TradePawn;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PawnTraded;
import dev.ecattez.shahmat.event.PieceCaptured;
import dev.ecattez.shahmat.event.PieceMoved;
import dev.ecattez.shahmat.event.PiecePositioned;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChessGame {

    private static final PieceFactory PIECE_FACTORY = new PieceBox(); // fixme: change it

    private static final PieceTypeVisitor<MovingStrategy> MOVING_STRATEGIES = new MovingRules();

    private static final PieceColorVisitor<Integer> TRADING_RANKS = new PawnTradingRankVisitor();

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

        if (canBeTraded(pieceToMove, to)) {
            events.add(
                new TradeProposed(to)
            );
        }

        return events;
    }

    public static List<BoardEvent> trade(
        List<BoardEvent> history,
        TradePawn command
    ) throws RulesViolation {
        PieceType typeOfTrade = command.typeOfTrade;

        if (isTryingToTradeForAKing(typeOfTrade)) {
            throw new TradeRefused(TradeRefused.Reason.TRADE_FOR_A_KING);
        }

        Square location = command.location;
        Piece pieceToBeTraded = replay(history)
            .getPiece(location)
            .orElseThrow(() -> new NoPieceOnSquare(location));

        if (!canBeTraded(pieceToBeTraded, location)) {
            throw new TradeRefused(TradeRefused.Reason.PIECE_IS_NOT_TRADABLE);
        }

        return Collections.singletonList(
            new PawnTraded(
                location,
                PIECE_FACTORY.createPiece(typeOfTrade, pieceToBeTraded.color)
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
            } else if (past instanceof TradeProposed) {
                board.apply((TradeProposed) past);
            } else if (past instanceof PawnTraded) {
                board.apply((PawnTraded) past);
            }
        }
        return board;
    };

    private static boolean canBeMoved(Piece pieceOnBoard, Square from, Square to, Board board) {
        return pieceOnBoard.type.accept(MOVING_STRATEGIES)
            .canMove(pieceOnBoard, from, to, board);
    }

    private static boolean isTryingToTradeForAKing(PieceType typeOfTrade) {
        return PieceType.KING.equals(typeOfTrade);
    }

    private static boolean canBeTraded(Piece pieceOnBoard, Square location) {
        return pieceOnBoard.isOfType(PieceType.PAWN)
            && pieceOnBoard.color.accept(TRADING_RANKS)
                .equals(location.rank.value);
    }

}
