package dev.ecattez.shahmat.domain.game.init;

import dev.ecattez.shahmat.domain.event.ChessEvent;

import java.util.List;

public interface BoardInitialization {

    List<ChessEvent> init();

}
