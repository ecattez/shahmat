package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.Board;
import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;
import dev.ecattez.shahmat.domain.game.BoardDecision;

import java.util.List;
import java.util.stream.Collectors;

public class AwareOfCheckMovingStrategy extends AbstractMovingStrategy {

    private MovingStrategy strategy;

    public AwareOfCheckMovingStrategy(MovingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public List<Movement> getAvailableMovements(Board board, Piece piece, Square from) {
        return strategy
            .getAvailableMovements(board, piece, from)
            .stream()
            .filter(move -> BoardDecision.wouldBePossible(board, move))
            .collect(Collectors.toList());
    }

}
