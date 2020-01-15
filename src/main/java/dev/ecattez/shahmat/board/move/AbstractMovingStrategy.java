package dev.ecattez.shahmat.board.move;

import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.board.move.Movement;
import dev.ecattez.shahmat.board.move.MovingStrategy;
import dev.ecattez.shahmat.event.BoardEvent;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMovingStrategy implements MovingStrategy {

    public Optional<Movement> findMovement(List<BoardEvent> history, Board board, Piece piece, Square from, Square to) {
        return getAvailableMovements(history, board, piece, from)
            .stream()
            .filter(move -> from.equals(move.from()) && to.equals(move.to()))
            .findFirst();
    }

}
