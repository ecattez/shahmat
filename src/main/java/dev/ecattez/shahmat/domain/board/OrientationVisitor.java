package dev.ecattez.shahmat.domain.board;

public interface OrientationVisitor<T> {

    T visitUpward();

    T visitDownward();

}
