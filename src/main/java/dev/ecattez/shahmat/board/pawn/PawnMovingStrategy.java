package dev.ecattez.shahmat.board.pawn;

import dev.ecattez.shahmat.board.AbstractMovingStrategy;
import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.Orientation;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PawnMovingStrategy extends AbstractMovingStrategy {

    private static final Direction[] CAPTURABLE_DIRECTIONS = new Direction[]{
        Direction.FORWARD_LEFT,
        Direction.FORWARD_RIGHT
    };

    private static final PawnStartingRankVisitor STARTING_RANK_VISITOR = new PawnStartingRankVisitor();

    @Override
    public List<Square> availableMoves(Piece piece, Square from, Board board) {
        List<Square> moves = new LinkedList<>();
        Orientation orientation = piece.orientation();
        int numberOfMoves = getNumberOfMoves(piece, from);

        moves.addAll(getVacantNeighbours(from, orientation, board, numberOfMoves, new ArrayList<>()));
        moves.addAll(getCapturablePieces(piece, from, orientation, board));

        return moves;
    }

    private List<Square> getVacantNeighbours(Square from, Orientation orientation, Board board, int numberOfMoves, List<Square> neighboursKept) {
        if (numberOfMoves < 1) {
            return neighboursKept;
        }

        Optional<Square> neighbourOptional = from.neighbour(Direction.FORWARD, orientation);
        if (!neighbourOptional.isPresent()) {
            return neighboursKept;
        }

        Square forwardSquare = neighbourOptional.get();
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
    }

    private List<Square> getCapturablePieces(Piece piece, Square from, Orientation orientation, Board board) {
        return Arrays.stream(CAPTURABLE_DIRECTIONS)
            .map(direction -> from.neighbour(direction, orientation))
            .flatMap(Optional::stream)
            .filter(neighbour -> board.isOpponentOf(neighbour, piece))
            .collect(Collectors.toList());
    }

    private boolean isOnItsStartingRank(Piece piece, Square location) {
        return piece.color.accept(STARTING_RANK_VISITOR) == location.rank.value;
    }

    private int getNumberOfMoves(Piece piece, Square location) {
        return isOnItsStartingRank(piece, location) ? 2 : 1;
    }

}
