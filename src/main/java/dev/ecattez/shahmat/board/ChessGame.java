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

    public static List<BoardEvent> move(List<BoardEvent> history, MovePiece command) throws InvalidMove {
        Square from = command.from;
        Square to = command.to;

        if (from.equals(to)) {
            throw new InvalidMove("Current position and destination are the same square");
        }

        List<BoardEvent> events = new LinkedList<>();
        Board board = replay(history);
        Piece piece = command.piece;

        board.getPiece(from)
            .filter(pieceOnBoard ->
                pieceOnBoard.equals(piece)
                    && canBeMoved(pieceOnBoard, from, to, board)
            )
            .ifPresent(pieceToMove -> {
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
            });
        ;

        return events;
    }

    public static List<BoardEvent> trade(List<BoardEvent> history, TradePawn command) throws TradeRefused {
        PieceType typeOfTrade = command.typeOfTrade;
        Square location = command.location;

        // Should never be null
        if (typeOfTrade == null) {
            return Collections.emptyList();
        }

        if (PieceType.KING.equals(typeOfTrade)) {
            throw new TradeRefused(location);
        }

        return replay(history)
            .getPiece(location)
            .filter(pieceOnBoard ->
                canBeTraded(pieceOnBoard, location)
            )
            .map(pawnToTrade ->
                new PawnTraded(
                    location,
                    PIECE_FACTORY.createPiece(typeOfTrade, pawnToTrade.color)
                )
            )
            .map(Collections::<BoardEvent>singletonList)
            .orElse(Collections.emptyList())
        ;
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

    private static boolean canBeTraded(Piece pieceOnBoard, Square location) {
        return pieceOnBoard.isOfType(PieceType.PAWN)
            && pieceOnBoard.color.accept(TRADING_RANKS)
                .equals(location.rank.value);
    }

}
