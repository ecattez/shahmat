package dev.ecattez.shahmat.board;

import java.util.List;

public interface MovingStrategy {

    boolean canMove(Piece piece, Square from, Square to, Board board);

    List<Square> availableMoves(Piece piece, Square from, Board board);

}
