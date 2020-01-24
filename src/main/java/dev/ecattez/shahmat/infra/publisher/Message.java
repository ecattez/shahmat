package dev.ecattez.shahmat.infra.publisher;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.aggregate.ChessGameId;

import java.util.Objects;

public class Message<E extends ChessEvent> {

    public final ChessGameId id;
    public final E event;

    public Message(ChessGameId id, E event) {
        this.id = id;
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message<?> that = (Message<?>) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event);
    }
}
