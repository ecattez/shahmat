package dev.ecattez.shahmat.board.move;

import dev.ecattez.shahmat.board.Board;
import dev.ecattez.shahmat.board.Piece;
import dev.ecattez.shahmat.board.Square;
import dev.ecattez.shahmat.event.BoardEvent;

import java.util.List;
import java.util.Optional;

public interface MovingStrategy {

    Optional<Movement> findMovement(List<BoardEvent> history, Board board, Piece piece, Square from, Square to);

    List<Movement> getAvailableMovements(List<BoardEvent> history, Board board, Piece piece, Square from);

}
