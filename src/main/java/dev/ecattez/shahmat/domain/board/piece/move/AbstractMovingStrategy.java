package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.Optional;

public abstract class AbstractMovingStrategy implements MovingStrategy {

    public Optional<Movement> findMovement(Board board, Piece piece, Square from, Square to) {
        return getAvailableMovements(board, piece, from)
            .stream()
            .filter(move -> from.equals(move.from()) && to.equals(move.to()))
            .findFirst();
    }

}
