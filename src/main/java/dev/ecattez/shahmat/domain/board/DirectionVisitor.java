package dev.ecattez.shahmat.domain.board;

public interface DirectionVisitor<T> {

    T visitForward();

    T visitForwardLeft();

    T visitForwardRight();

    T visitShiftLeft();

    T visitShiftRight();

    T visitBackward();

    T visitBackwardLeft();

    T visitBackwardRight();

}
