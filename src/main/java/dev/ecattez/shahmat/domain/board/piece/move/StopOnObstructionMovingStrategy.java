package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StopOnObstructionMovingStrategy extends AbstractMovingStrategy {

    private final Direction[] movingDirections;

    public StopOnObstructionMovingStrategy(Direction... movingDirections) {
        this.movingDirections = movingDirections;
    }

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        Orientation orientation = piece.orientation();
        return Stream.of(movingDirections)
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
            new StandardMove(
                piece,
                from,
                next
            )
        );
    }

    private void addCaptureIfOpponentPresent(Board board, Piece piece, Square from, List<Movement> neighbourKept, Square next) {
        if (board.hasOpponent(next, piece)) {
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
