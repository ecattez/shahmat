package dev.ecattez.shahmat.domain.board.piece.pawn;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.Direction;
import dev.ecattez.shahmat.domain.board.Orientation;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.piece.move.AbstractMovingStrategy;
import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.EnPassant;
import dev.ecattez.shahmat.domain.board.piece.move.MoveOnVacant;
import dev.ecattez.shahmat.domain.board.piece.move.Movement;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PawnMovingStrategy extends AbstractMovingStrategy {

    private final PawnMovingForwardStrategy movingForwardStrategy;
    private final PawnCaptureStrategy captureStrategy;
    private final EnPassantMovingStrategy enPassantMovingStrategy;

    public PawnMovingStrategy() {
        this.movingForwardStrategy = new PawnMovingForwardStrategy();
        this.captureStrategy = new PawnCaptureStrategy();
        this.enPassantMovingStrategy = new EnPassantMovingStrategy();
    }

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        return Stream.of(
            movingForwardStrategy.getAvailableMovements(board, piece, from),
            captureStrategy.getAvailableMovements(board, piece, from),
            enPassantMovingStrategy.getAvailableMovements(board, piece, from)
        )
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private static class PawnMovingForwardStrategy extends AbstractMovingStrategy {

        @Override
        public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
            return getForward(
                from,
                piece.orientation(),
                board,
                getNumberOfMoves(piece, from),
                new LinkedList<>()
            )
                .stream()
                .map(to -> new MoveOnVacant(piece, from, to))
                .collect(Collectors.toList());
        }

        private List<Square> getForward(
            Square from,
            Orientation orientation,
            Board board,
            int numberOfMoves,
            List<Square> neighboursKept
        ) {
            if (numberOfMoves < 1) {
                return neighboursKept;
            }
            return from.findNeighbour(Direction.FORWARD, orientation)
                .map(forwardSquare -> {
                    if (board.isVacant(forwardSquare)) {
                        neighboursKept.add(forwardSquare);
                    }
                    return getForward(
                        forwardSquare,
                        orientation,
                        board,
                        numberOfMoves - 1,
                        neighboursKept
                    );
                })
                .orElse(neighboursKept);
        }

        private boolean isOnItsStartingRank(Piece piece, Square location) {
            return piece
                .color()
                .accept(PawnStartingRankVisitor.getInstance())
                .equals(location.rank);
        }

        private int getNumberOfMoves(Piece piece, Square location) {
            return isOnItsStartingRank(piece, location) ? 2 : 1;
        }

    }

    private static class PawnCaptureStrategy extends AbstractMovingStrategy {

        private static final Direction[] CAPTURABLE_DIRECTIONS = new Direction[]{
            Direction.FORWARD_LEFT,
            Direction.FORWARD_RIGHT
        };

        @Override
        public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
            return Arrays.stream(CAPTURABLE_DIRECTIONS)
                .map(direction -> from.findNeighbour(direction, piece.orientation()))
                .flatMap(Optional::stream)
                .filter(neighbour -> board.hasOpponent(neighbour, piece))
                .map(neighbour -> new Capture(piece, from, neighbour, board.getPiece(neighbour)))
                .collect(Collectors.toList());
        }

    }

    private static class EnPassantMovingStrategy extends AbstractMovingStrategy {

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

}
