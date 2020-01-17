package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Orientation;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.PieceType;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.move.AbstractMovingStrategy;
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
        return piece.color.accept(EN_PASSANT_VISITOR).equals(location.rank);
    }

    @Override
    public List<Movement> getAvailableMovements(List<BoardEvent> history, Board board, Piece piece, Square from) {
        if (!isOnEnPassantRank(piece, from) || !piece.isOfType(PieceType.PAWN)) {
            return Collections.emptyList();
        }

        BoardEvent lastEvent = history.get(history.size() - 1);
        Piece opponentPawn = new Pawn(piece.color.opposite());
        Orientation currentOrientation = piece.orientation();

        return Arrays.stream(EN_PASSANT_CAPTURABLE_DIRECTIONS)
            .map(direction -> from.findNeighbour(direction, currentOrientation))
            .flatMap(Optional::stream)
            .filter(neighbourSquare -> canBeCapturedEnPassant(opponentPawn, neighbourSquare, lastEvent))
            .map(opponentSquare -> findSquareBackwardTheOpponent(opponentPawn, opponentSquare))
            .flatMap(Optional::stream)
            .map(selectedSquare -> new EnPassant(piece, from, selectedSquare, opponentPawn))
            .collect(Collectors.toList());
    }

    private boolean canBeCapturedEnPassant(Piece opponentPawn, Square neighbourSquare, BoardEvent lastEvent) {
        return neighbourSquare
            .findNeighbour(Direction.BACKWARD, opponentPawn.orientation, 2)
            .map(from -> new PieceMoved(
                opponentPawn,
                from,
                neighbourSquare
            ))
            .filter(lastEvent::equals)
            .isPresent();
    }

    private Optional<Square> findSquareBackwardTheOpponent(Piece opponentPawn, Square location) {
        return location.findNeighbour(Direction.BACKWARD, opponentPawn.orientation());
    }

}
