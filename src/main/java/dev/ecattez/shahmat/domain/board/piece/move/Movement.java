package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.piece.Piece;
import dev.ecattez.shahmat.domain.board.square.Square;

public interface Movement {

    Piece piece();

    Square from();

    Square to();

    <T> T accept(MovementVisitor<T> visitor);

}
