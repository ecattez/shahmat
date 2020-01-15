package dev.ecattez.shahmat.event;

import dev.ecattez.shahmat.board.Direction;
import dev.ecattez.shahmat.board.violation.OutsideSquare;
import dev.ecattez.shahmat.board.move.Capture;
import dev.ecattez.shahmat.board.move.EnPassant;
import dev.ecattez.shahmat.board.move.MoveOnVacant;
import dev.ecattez.shahmat.board.move.MovementVisitor;

import java.util.List;

public class MovementToEventVisitor implements MovementVisitor<List<BoardEvent>> {

    @Override
    public List<BoardEvent> visit(MoveOnVacant move) {
        return List.of(
            new PieceMoved(
                move.piece,
                move.from,
                move.to
            )
        );
    }

    @Override
    public List<BoardEvent> visit(Capture move) {
        return List.of(
            new PieceMoved(
                move.piece,
                move.from,
                move.to
            ),
            new PieceCaptured(
                move.captured,
                move.piece,
                move.to
            )
        );
    }

    @Override
    public List<BoardEvent> visit(EnPassant move) {
        return List.of(
            new PieceMoved(
                move.piece,
                move.from,
                move.to
            ),
            new PieceCaptured(
                move.captured,
                move.piece,
                move.to
                    .findNeighbour(Direction.FORWARD, move.captured.orientation())
                    .orElseThrow(OutsideSquare::new)
            )
        );
    }

}
