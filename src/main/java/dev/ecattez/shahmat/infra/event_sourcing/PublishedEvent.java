package fr.leroymerlin.configurators.rollershutter.configuration.infra.event_sourcing;

import fr.leroymerlin.configurators.rollershutter.configuration.domain.ConfigurationEvent;
import fr.leroymerlin.configurators.rollershutter.configuration.infra.ConfigurationId;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@EqualsAndHashCode
@ToString
public class PublishedEvent<E extends ConfigurationEvent> implements ResolvableTypeProvider {

    public final ConfigurationId configurationId;
    public final E payload;
    public final long timestamp;

    public PublishedEvent(ConfigurationId configurationId, E payload, long timestamp) {
        this.configurationId = configurationId;
        this.payload = payload;
        this.timestamp = timestamp;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(), ResolvableType.forInstance(payload)
        );
    }
}
