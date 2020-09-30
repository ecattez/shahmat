package dev.ecattez.shahmat.infra.event_sourcing;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
public class ChessGamePubSub implements PubSub {

    private final ChessEventStore eventStore;
    private final ApplicationEventPublisher publisher;
    private Supplier<Long> timestampProvider;

    public ChessEventPublisher(
      ChessEventStore eventStore,
      ApplicationEventPublisher publisher,
      Supplier<Long> timestampProvider
    ) {
        this.eventStore = eventStore;
        this.publisher = publisher;
        this.timestampProvider = timestampProvider;
    }

    public Long sequence(ChessGameId chessGameId) {
        return (long) eventStore.history(chessGameId).size();
    }

    public void publish(ChessGameId chessGameId, List<ChessEvent> events) {
        eventStore.store(
          chessGameId,
          sequence(chessGameId),
          events
        );
        events
          .stream()
          .map(event -> toPublishedEvent(chessGameId, event))
          .forEach(publisher::publishEvent);
    }

    private <E extends ChessEvent> PublishedEvent<E> toPublishedEvent(ChessGameId chessGameId, E event) {
        return new PublishedEvent<E>(chessGameId, event, this.timestampProvider.get());
    }


}
