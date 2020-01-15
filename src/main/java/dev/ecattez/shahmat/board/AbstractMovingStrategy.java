package dev.ecattez.shahmat.board;

import dev.ecattez.shahmat.board.move.Movement;
import dev.ecattez.shahmat.board.move.MovingStrategy;
import dev.ecattez.shahmat.event.BoardEvent;

import java.util.List;
import java.util.Optional;

public abstract class AbstractMovingStrategy implements MovingStrategy {

    public Optional<Movement> getMovement(Piece piece, Square from, Square to, Board board, List<BoardEvent> history) {
        return getAvailableMovements(piece, from, board, history)
            .stream()
            .filter(move -> from.equals(move.from()) && to.equals(move.to()))
            .findFirst();
    }

}
