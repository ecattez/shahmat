package dev.ecattez.shahmat.board;

public abstract class AbstractMovingStrategy implements MovingStrategy {

    @Override
    public boolean canMove(Piece piece, Square from, Square to, Board board) {
        return availableMoves(piece, from, board).contains(to);
    }

}
