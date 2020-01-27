package dev.ecattez.shahmat.domain.board.piece.move;

import dev.ecattez.shahmat.domain.board.square.Square;

public interface Movement {

    Square from();

    Square to();

    <T> T accept(MovementVisitor<T> visitor);

}
