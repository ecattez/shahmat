package dev.ecattez.shahmat.domain.board.piece.knight;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.move.AbstractMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.MoveOnVacant;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KnightMovingStrategy extends AbstractMovingStrategy {

    private static final PerpendicularDirectionVisitor PERPENDICULAR_DIRECTION_VISITOR = new PerpendicularDirectionVisitor();

    public static final Direction[] MOVING_DIRECTIONS = new Direction[]{
        Direction.FORWARD,
        Direction.SHIFT_RIGHT,
        Direction.BACKWARD,
        Direction.SHIFT_LEFT
    };

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        List<Movement> moves = new LinkedList<>();
        Orientation orientation = piece.orientation();
        for (Direction direction: MOVING_DIRECTIONS) {
            squareTwoTimesInDirection(from, orientation, direction)
                .map(found -> findPerpendicularSquares(found, orientation, direction))
                .map(found -> found
                    .stream()
                    .filter(perpendicular -> !board.hasAlly(perpendicular, piece))
                    .map(perpendicular -> toMovement(board, piece, from, perpendicular))
                    .collect(Collectors.toList())
                ).ifPresent(moves::addAll);
        }
        return moves;
    }

    private Optional<Square> squareTwoTimesInDirection(Square from, Orientation orientation, Direction direction) {
        return from.findNeighbour(direction, orientation, 2);
    }

    private List<Square> findPerpendicularSquares(Square from, Orientation orientation, Direction direction) {
        return Stream.of(direction.accept(PERPENDICULAR_DIRECTION_VISITOR))
            .map(perpendicular -> from.findNeighbour(perpendicular, orientation))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    private Movement toMovement(Board board, Piece piece, Square from, Square perpendicular) {
        if (board.hasOpponent(perpendicular, piece)) {
            return new Capture(piece, from, perpendicular, board.getPiece(perpendicular));
        }
        return new MoveOnVacant(piece, from, perpendicular);
    }

}
