package dev.ecattez.shahmat.domain.game;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.PieceColor;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.violation.BoardAlreadyInitialized;
import dev.ecattez.shahmat.domain.board.violation.InvalidMove;
import dev.ecattez.shahmat.domain.board.violation.NoPieceOnSquare;
import dev.ecattez.shahmat.domain.board.violation.PieceNotOwned;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.board.violation.WrongPieceSelected;
import dev.ecattez.shahmat.domain.command.InitBoard;
import dev.ecattez.shahmat.domain.command.Move;
import dev.ecattez.shahmat.domain.event.BoardInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.event.TurnChanged;
import dev.ecattez.shahmat.domain.game.init.GameInitialization;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessGame {

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

        ChessEvent move = BoardDecision.move(board, from, to, pieceToMove, command.promotedTo);

        List<ChessEvent> returnedEvents = new LinkedList<>();
        returnedEvents.add(move);

        Board futureBoard = BoardDecision.replay(history, move);
        if (!futureBoard.isOver()) {
            returnedEvents.add(new TurnChanged(BoardDecision.whoseNextTurnIs(board)));
        }

        return returnedEvents;
    }

}
