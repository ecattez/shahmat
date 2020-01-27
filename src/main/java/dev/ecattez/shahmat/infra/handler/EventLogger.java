package dev.ecattez.shahmat.infra.handler;

import com.google.common.eventbus.Subscribe;
import dev.ecattez.shahmat.domain.event.ChessEvent;
import dev.ecattez.shahmat.infra.publisher.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventLogger implements EventHandler<ChessEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogger.class);

    @Subscribe
    @Override
    public void handle(Message<ChessEvent> message) {
        LOGGER.debug("Handling event id={} event={}", message.id, message.event);
    }
}
