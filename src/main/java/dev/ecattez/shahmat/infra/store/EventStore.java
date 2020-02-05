package dev.ecattez.shahmat.infra.store;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;

import java.util.List;
import java.util.Set;

public interface EventStore {

    void store(ChessGameId id, Long sequence, List<ChessEvent> events) throws SequenceAlreadyExists;

    List<ChessEvent> history(final ChessGameId id);

    Set<ChessGameId> aggregateIds();

}
