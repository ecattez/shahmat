package dev.ecattez.shahmat.board;

public interface OrientationVisitor<T> {

    T visitBlackView();

    T visitWhiteView();

}
