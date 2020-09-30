package dev.ecattez.shahmat.domain.command;

import dev.ecattez.shahmat.ddd.Port;
import dev.ecattez.shahmat.domain.board.violation.BoardAlreadyInitialized;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.domain.game.GameType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@Port
public interface BoardPort {

  List<ChessEvent> initialize(List<ChessEvent> history, InitBoard command) throws BoardAlreadyInitialized;


  @Builder
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @EqualsAndHashCode
  @ToString
  class InitBoard {
      @NonNull public final GameType gameType;
  }
}
