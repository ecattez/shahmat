package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.EnPassant;
import dev.ecattez.shahmat.domain.board.piece.move.MoveOnVacant;
import dev.ecattez.shahmat.domain.board.piece.move.MovementVisitor;

import java.util.List;

public class MovementToEventVisitor implements MovementVisitor<List<ChessEvent>> {

    @Override
    public List<ChessEvent> visit(MoveOnVacant move) {
        return List.of(
            new PieceMoved(
                move.piece,
                move.from,
                move.to
            )
        );
    }

    @Override
    public List<ChessEvent> visit(Capture move) {
        return List.of(
            new PieceCaptured(
                move.captured,
                move.to,
                move.piece,
                move.from
            )
        );
    }

    @Override
    public List<ChessEvent> visit(EnPassant move) {
        return List.of(
            new PieceCapturedEnPassant(
                move.captured,
                move.to,
                move.piece,
                move.from
            )
        );
    }

}
