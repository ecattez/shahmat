package dev.ecattez.shahmat.domain.game;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.piece.PieceType;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.BoardAlreadyInitialized;
import dev.ecattez.shahmat.domain.board.violation.ImpossibleToMove;
import dev.ecattez.shahmat.domain.board.violation.InvalidMove;
import dev.ecattez.shahmat.domain.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.domain.board.violation.PieceNotOwned;
import dev.ecattez.shahmat.domain.board.violation.PromotionMustBeDone;
import dev.ecattez.shahmat.domain.board.violation.PromotionRefused;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.board.violation.WrongPieceSelected;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.command.Promote;
import dev.ecattez.shahmat.domain.event.BoardInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.MovementToEventVisitor;
import dev.ecattez.shahmat.domain.event.PawnPromoted;
import dev.ecattez.shahmat.domain.event.PromotionProposed;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.init.GameInitialization;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessGame {

    private static final MovementToEventVisitor EVENTS_FROM_MOVEMENT = new MovementToEventVisitor();

    private static final GameInitialization GAME_INITIALIZATION = new GameInitialization();

    public static List<ChessEvent> initalizeBoard(
        List<ChessEvent> history,
        InitBoard command
    ) throws BoardAlreadyInitialized {
        if (BoardDecision.replay(history).isPlaying()) {
            throw new BoardAlreadyInitialized();
        }

        GameType gameType = command.gameType;
        return Stream.of(
            gameType
                .accept(GAME_INITIALIZATION)
                .init(),
            List.of(
                new BoardInitialized(command.gameType),
                new TurnChanged(PieceColor.WHITE)
            )
        )
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    public static List<ChessEvent> move(
        List<ChessEvent> history,
        Move command
    ) throws RulesViolation {
        Square from = command.from;
        Square to = command.to;

        if (from.equals(to)) {
            throw new InvalidMove("Current position and destination are the same square");
        }

        Board board = BoardDecision.replay(history);

        if (board.isPromoting()) {
            throw new PromotionMustBeDone();
        }

        if (board.isVacant(from)) {
            throw new NoPieceOnSquare(from);
        }

        Piece pieceToMove = board.getPiece(from);
        if (!BoardDecision.isOwnedByCurrentPlayer(board, pieceToMove)) {
            throw new PieceNotOwned(from);
        }
        if (!pieceToMove.isOfType(command.type)) {
            throw new WrongPieceSelected(from);
        }

        List<ChessEvent> events = BoardDecision.findMovement(board, from, to, pieceToMove)
            .stream()
            .map(move -> move.accept(EVENTS_FROM_MOVEMENT))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (events.isEmpty()) {
            throw new ImpossibleToMove(pieceToMove, from, to);
        }

        events.add(
            BoardDecision.canBePromoted(to, pieceToMove)
                ? new PromotionProposed(to)
                : new TurnChanged(
                    BoardDecision.whoseNextTurnIs(board)
                )
        );

        return events;
    }

    public static List<ChessEvent> promote(
        List<ChessEvent> history,
        Promote command
    ) throws RulesViolation {
        PieceType typeOfPromotion = command.typeOfPromotion;

        if (!BoardDecision.canPromote(typeOfPromotion)) {
            throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_PROMOTE);
        }

        Board board = BoardDecision.replay(history);
        Square location = command.location;

        if (!board.isPromotingIn(location)) {
            throw new PromotionRefused(PromotionRefused.Reason.PIECE_CAN_NOT_BE_PROMOTED);
        }

        return List.of(
            new PawnPromoted(
                location,
                typeOfPromotion
            ),
            new TurnChanged(
                BoardDecision.whoseNextTurnIs(board)
            )
        );
    }

}
