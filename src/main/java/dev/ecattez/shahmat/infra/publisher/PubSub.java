package dev.ecattez.shahmat.infra.publisher;

import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.store.SequenceAlreadyExists;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;

import java.util.List;

public interface PubSub {

    void dispatch(
        ChessGameId id,
        Long sequence,
        List<ChessEvent> events
    ) throws RulesViolation, SequenceAlreadyExists;

}
