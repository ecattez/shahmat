package dev.ecattez.shahmat.domain.event;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EventListener {

    private Map<Class<? extends ChessEvent>, Consumer<ChessEvent>> appliers = new LinkedHashMap<>();

    protected <E extends ChessEvent> void onEvent(Class<E> eventClass, Consumer<E> applier) {
        appliers.put(
            eventClass,
            (event) -> applier.accept(eventClass.cast(event))
        );
    }

    public void apply(ChessEvent event) {
        Optional.of(event)
            .map(Object::getClass)
            .map(eventType -> appliers.get(eventType))
            .ifPresent(applier -> applier.accept(event))
        ;
    }

}
