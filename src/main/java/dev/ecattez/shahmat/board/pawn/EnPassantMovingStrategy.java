package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.AbstractMovingStrategy;
import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Orientation;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.move.EnPassant;
import dev.ecattez.shahmat.board.move.Movement;
import dev.ecattez.shahmat.event.BoardEvent;
import dev.ecattez.shahmat.event.PieceMoved;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnPassantMovingStrategy extends AbstractMovingStrategy {

    private static final Direction[] EN_PASSANT_CAPTURABLE_DIRECTIONS = new Direction[]{
        Direction.SHIFT_LEFT,
        Direction.SHIFT_RIGHT
    };

    private static final EnPassantRankVisitor EN_PASSANT_VISITOR = new EnPassantRankVisitor();

    private boolean isOnEnPassantRank(Piece piece, Square location) {
        return piece.color.accept(EN_PASSANT_VISITOR) == location.rank.value;
    }

    @Override
    public List<Movement> getAvailableMovements(Piece piece, Square from, Board board, List<BoardEvent> history) {
        if (!isOnEnPassantRank(piece, from) || !piece.isOfType(PieceType.PAWN)) {
            return Collections.emptyList();
        }

        BoardEvent lastEvent = history.get(history.size() - 1);
        Piece opponentPawn = new Pawn(piece.color.opposite());
        Orientation currentOrientation = piece.orientation();

        return Arrays.stream(EN_PASSANT_CAPTURABLE_DIRECTIONS)
            .map(direction -> from.neighbour(direction, currentOrientation))
            .flatMap(Optional::stream)
            .filter(neighbourSquare -> canBeCapturedEnPassant(opponentPawn, neighbourSquare, lastEvent))
            .map(opponentSquare -> getSquareBackwardTheOpponent(opponentPawn, opponentSquare))
            .flatMap(Optional::stream)
            .map(selectedSquare -> new EnPassant(piece, from, selectedSquare, opponentPawn))
            .collect(Collectors.toList());
    }

    private boolean canBeCapturedEnPassant(Piece opponentPawn, Square neighbourSquare, BoardEvent lastEvent) {
        return neighbourSquare
            .neighbour(Direction.BACKWARD, opponentPawn.orientation, 2)
            .map(from -> new PieceMoved(
                opponentPawn,
                from,
                neighbourSquare
            ))
            .filter(lastEvent::equals)
            .isPresent();
    }

    private Optional<Square> getSquareBackwardTheOpponent(Piece opponentPawn, Square location) {
        return location.neighbour(Direction.BACKWARD, opponentPawn.orientation());
    }

}
