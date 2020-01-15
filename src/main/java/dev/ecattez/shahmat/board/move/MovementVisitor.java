package dev.ecattez.shahmat.board.move;

public interface MovementVisitor<T> {

    T visit(MoveOnVacant move);

    T visit(Capture move);

    T visit(EnPassant move);

}
