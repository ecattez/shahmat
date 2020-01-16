package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Orientation;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.move.AbstractMovingStrategy;
import dev.ecattez.shahmat.board.move.Capture;
import dev.ecattez.shahmat.board.move.MoveOnVacant;
import dev.ecattez.shahmat.board.move.Movement;
import dev.ecattez.shahmat.event.BoardEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PawnMovingStrategy extends AbstractMovingStrategy {

    private static final Direction[] CAPTURABLE_DIRECTIONS = new Direction[]{
        Direction.FORWARD_LEFT,
        Direction.FORWARD_RIGHT
    };

    private static final PawnStartingRankVisitor STARTING_RANK_VISITOR = new PawnStartingRankVisitor();

    private static final EnPassantMovingStrategy EN_PASSANT_MOVING_STRATEGY = new EnPassantMovingStrategy();

    @Override
    public List<Movement> getAvailableMovements(List<BoardEvent> history, Board board, Piece piece, Square from) {
        return Stream.of(
            getVacantNeighbours(piece, from, board),
            getCapturablePieces(piece, from, board),
            getCapturablePiecesEnPassant(piece, from, board, history)
        )
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private List<Movement> getVacantNeighbours(Piece piece, Square from, Board board) {
        return getVacantNeighbours(from, piece.orientation(), board, getNumberOfMoves(piece, from), new ArrayList<>())
            .stream()
            .map(to -> new MoveOnVacant(piece, from, to))
            .collect(Collectors.toList());
    }

    private List<Square> getVacantNeighbours(Square from, Orientation orientation, Board board, int numberOfMoves, List<Square> neighboursKept) {
        if (numberOfMoves < 1) {
            return neighboursKept;
        }
        return from.findNeighbour(Direction.FORWARD, orientation)
            .map(forwardSquare -> {
                if (board.isVacant(forwardSquare)) {
                    neighboursKept.add(forwardSquare);
                }
                return getVacantNeighbours(
                    forwardSquare,
                    orientation,
                    board,
                    numberOfMoves - 1,
                    neighboursKept
                );
            })
            .orElse(neighboursKept);
    }

    private List<Movement> getCapturablePieces(Piece piece, Square from, Board board) {
        return Arrays.stream(CAPTURABLE_DIRECTIONS)
            .map(direction -> from.findNeighbour(direction, piece.orientation()))
            .flatMap(Optional::stream)
            .filter(neighbour -> board.isOpponentOf(neighbour, piece))
            .map(neighbour -> new Capture(piece, from, neighbour, board.getPiece(neighbour)))
            .collect(Collectors.toList());
    }

    private List<Movement> getCapturablePiecesEnPassant(Piece piece, Square from, Board board, List<BoardEvent> history) {
        return EN_PASSANT_MOVING_STRATEGY.getAvailableMovements(history, board, piece, from);
    }

    private boolean isOnItsStartingRank(Piece piece, Square location) {
        return piece.color.accept(STARTING_RANK_VISITOR).equals(location.rank);
    }

    private int getNumberOfMoves(Piece piece, Square location) {
        return isOnItsStartingRank(piece, location) ? 2 : 1;
    }

}
