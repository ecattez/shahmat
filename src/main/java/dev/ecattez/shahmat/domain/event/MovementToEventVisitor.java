package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.piece.move.Capture;
import dev.ecattez.shahmat.domain.board.piece.move.EnPassant;
import dev.ecattez.shahmat.domain.board.piece.move.MovementVisitor;
import dev.ecattez.shahmat.domain.board.piece.move.StandardMove;

public class MovementToEventVisitor implements MovementVisitor<ChessMoveEvent> {

    @Override
    public ChessMoveEvent visit(StandardMove move) {
        return new PieceMoved(
            move.piece(),
            move.from(),
            move.to()
        );
    }

    @Override
    public ChessMoveEvent visit(Capture move) {
        return new PieceCaptured(
            move.captured(),
            move.to(),
            move.piece(),
            move.from()
        );
    }

    @Override
    public ChessMoveEvent visit(EnPassant move) {
        return new PieceCapturedEnPassant(
            move.captured(),
            move.to(),
            move.piece(),
            move.from()
        );
    }

}
