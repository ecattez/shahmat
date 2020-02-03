package dev.ecattez.shahmat.infra.store;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ChessGameEventStore implements EventStore {

    private Map<ChessGameId, List<ChessEvent>> eventsPerId = new LinkedHashMap<>();

    @Override
    public void store(ChessGameId id, Long sequence, List<ChessEvent> events) throws SequenceAlreadyExists {
        List<ChessEvent> history = history(id);
        if (sequence < history.size()) {
            throw new SequenceAlreadyExists(sequence);
        }
        eventsPerId.put(
            id,
            Stream.of(history, events)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<ChessEvent> history(ChessGameId id) {
        return Optional.ofNullable(eventsPerId.get(id))
            .orElse(Collections.emptyList());
    }

    @Override
    public Set<ChessGameId> aggregateIds() {
        return eventsPerId.keySet();
    }

}
