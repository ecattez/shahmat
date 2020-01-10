package dev.ecattez.shahmat.board;

public interface OrientationVisitor<T> {

    T visitUpward();

    T visitDownward();

}
