package dev.ecattez.shahmat.board.move;

import dev.ecattez.shahmat.board.Square;

public interface Movement {

    Square from();

    Square to();

    <T> T accept(MovementVisitor<T> visitor);

}
