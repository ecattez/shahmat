package dev.ecattez.shahmat.domain.game;

public interface GameTypeVisitor<T> {

    T visitClassical();

}
