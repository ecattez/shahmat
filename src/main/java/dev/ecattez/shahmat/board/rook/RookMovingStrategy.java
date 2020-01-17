package dev.ecattez.shahmat.board.rook;

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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RookMovingStrategy extends AbstractMovingStrategy {

    private static final Direction[] MOVING_DIRECTIONS = new Direction[]{
        Direction.FORWARD,
        Direction.BACKWARD,
        Direction.SHIFT_LEFT,
        Direction.SHIFT_RIGHT
    };

    @Override
    public List<Movement> getAvailableMovements(List<BoardEvent> history, Board board, Piece piece, Square from) {
        Orientation orientation = piece.orientation();
        return Stream.of(MOVING_DIRECTIONS)
            .map(direction -> reachUntilObstruction(
                board,
                piece,
                from,
                from,
                direction,
                orientation,
                new ArrayList<>()
            ))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<Movement> reachUntilObstruction(
        Board board,
        Piece piece,
        Square from,
        Square location,
        Direction direction,
        Orientation orientation,
        List<Movement> neighbourKept
    ) {
        return location.findNeighbour(direction, orientation)
            .map(next -> {
                if (!board.isVacant(next)) {
                    addCaptureIfOpponentPresent(board, piece, from, neighbourKept, next);
                    return neighbourKept;
                } else {
                    addMoveOnVacant(piece, from, neighbourKept, next);
                    return reachUntilObstruction(
                        board,
                        piece,
                        from,
                        next,
                        direction,
                        orientation,
                        neighbourKept
                    );
                }
            })
            .orElse(neighbourKept);
    }

    private void addMoveOnVacant(Piece piece, Square from, List<Movement> neighbourKept, Square next) {
        neighbourKept.add(
            new MoveOnVacant(
                piece,
                from,
                next
            )
        );
    }

    private void addCaptureIfOpponentPresent(Board board, Piece piece, Square from, List<Movement> neighbourKept, Square next) {
        if (board.isOpponentOf(next, piece)) {
            neighbourKept.add(
                new Capture(
                    piece,
                    from,
                    next,
                    board.getPiece(next)
                )
            );
        }
    }

}
