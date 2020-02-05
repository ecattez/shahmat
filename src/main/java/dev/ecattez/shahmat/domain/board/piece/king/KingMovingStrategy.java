package dev.ecattez.shahmat.domain.board.piece.king;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.move.AbstractMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.MoveOnVacant;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KingMovingStrategy extends AbstractMovingStrategy {

    public static final Direction[] MOVING_DIRECTIONS = Direction.values();

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        Orientation orientation = piece.orientation();
        return Stream.of(MOVING_DIRECTIONS)
            .map(direction -> from.findNeighbour(direction, orientation))
            .flatMap(Optional::stream)
            .filter(neighbour -> !board.hasAlly(neighbour, piece))
            .map(neighbour -> toMovement(board, piece, from, neighbour))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    private Optional<Movement> toMovement(Board board, Piece piece, Square from, Square location) {
        if (board.isVacant(location)) {
            return Optional.of(new MoveOnVacant(piece, from, location));
        }
        if (board.hasOpponent(location, piece)) {
            return Optional.of(new Capture(piece, from, location, board.getPiece(location)));
        }
        return Optional.empty();
    }
}
