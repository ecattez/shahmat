package dev.ecattez.shahmat.infra.publisher;

import com.google.common.eventbus.EventBus;
import dev.ecattez.shahmat.domain.board.violation.RulesViolation;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.store.SequenceAlreadyExists;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import dev.ecattez.shahmat.infra.handler.EventHandler;
import dev.ecattez.shahmat.infra.store.EventStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChessGamePubSub implements PubSub {

    private final EventBus eventBus;
    private final EventStore eventStore;

    public ChessGamePubSub(
        EventStore eventStore,
        List<EventHandler> eventHandlers
    ) {
        this.eventBus = new EventBus();
        this.eventStore = eventStore;

        eventHandlers.forEach(eventBus::register);
    }

    @Override
    public void dispatch(
        ChessGameId id,
        Long sequence,
        List<ChessEvent> events
    ) throws RulesViolation, SequenceAlreadyExists {
        eventStore.store(id, sequence, events);
        events
            .stream()
            .map(event -> new Message<>(id, event))
            .forEach(eventBus::post);
    }


}
