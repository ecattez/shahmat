package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

import java.util.List;
import java.util.Optional;

public interface MovingStrategy {

    Optional<Movement> findMovement(Board board, Piece piece, Square from, Square to);

    List<Movement> getAvailableMovements(Board board, Piece piece, Square from);

}
