package dev.ecattez.shahmat.domain.event;

import dev.ecattez.shahmat.domain.board.square.Square;

public class KingCheckmated extends KingChecked {

    public KingCheckmated(ChessEvent cause, Square kingLocation) {
        super(cause, kingLocation);
    }

    @Override
    public String toString() {
        return cause + "#";
    }

}
