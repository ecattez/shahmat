package dev.ecattez.shahmat.infra.handler;

import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.publisher.Message;

public interface EventHandler<E extends ChessEvent> {

    void handle(Message<E> message);

}
