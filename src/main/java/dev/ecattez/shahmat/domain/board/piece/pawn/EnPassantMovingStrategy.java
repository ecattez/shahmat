package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.board.piece.move.AbstractMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.move.EnPassant;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EnPassantMovingStrategy extends AbstractMovingStrategy {

    private static final Direction[] EN_PASSANT_NEIGHBOUR_DIRECTIONS = new Direction[]{
        Direction.SHIFT_LEFT,
        Direction.SHIFT_RIGHT
    };

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        Orientation opponentOrientation = piece.orientation().reverse();

        return board.findEnPassantPieceLocation()
            .filter(enPassantPieceLocation -> canBeCapturedEnPassant(opponentOrientation, enPassantPieceLocation, from))
            .<Movement>map(enPassantPieceLocation -> new EnPassant(
                piece,
                from,
                enPassantPieceLocation.getNeighbour(Direction.BACKWARD, opponentOrientation),
                board.getPiece(enPassantPieceLocation)
            ))
            .map(List::of)
            .orElse(Collections.emptyList());
    }

    private boolean canBeCapturedEnPassant(Orientation opponentOrientation, Square enPassantPieceLocation, Square currentLocation) {
        return Stream.of(EN_PASSANT_NEIGHBOUR_DIRECTIONS)
            .map(direction -> enPassantPieceLocation.findNeighbour(direction, opponentOrientation))
            .flatMap(Optional::stream)
            .anyMatch(currentLocation::equals);
    }

}
