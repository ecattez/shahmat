package dev.ecattez.shahmat.domain.board.piece.move;

public interface MovementVisitor<T> {

    T visit(MoveOnVacant move);

    T visit(Capture move);

    T visit(EnPassant move);

}
