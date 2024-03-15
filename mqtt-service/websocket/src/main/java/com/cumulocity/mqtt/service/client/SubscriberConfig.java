package com.cumulocity.mqtt.service.client;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * {@link SubscriberConfig} provides configuration for {@link MqttSubscriber}
 */
@ToString(exclude = {"connectionListener"})
@FieldDefaults(makeFinal = true, level = PRIVATE)
@EqualsAndHashCode(exclude = "connectionListener")
public class SubscriberConfig {

    String id;

    String topic;

    String subscriber;

    ConnectionListener connectionListener;

    @Builder(builderMethodName = "subscriberConfig")
    private SubscriberConfig(final String id, final String topic, final String subscriber, final ConnectionListener connectionListener) {
        this.id = isBlank(id) ? String.format("subscriber:%s:%s", topic, subscriber) : id;
        this.topic = topic;
        this.subscriber = subscriber;
        this.connectionListener = isNull(connectionListener) ? new LoggingConnectionListener() : connectionListener;
    }

    /**
     * @return unique subscriber id used to identify the instance of {@link MqttSubscriber} in the {@link MqttClient} and {@link ConnectionListener}
     */
    public String getId() {
        return id;
    }

    /**
     * @return the topic to which instance of {@link MqttSubscriber} will connect to.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @return the subscriber name (consumer name) which will be used by the instance of {@link MqttSubscriber}.
     */
    public String getSubscriber() {
        return subscriber;
    }

    /**
     * @return connection listener which allows to monitor established connection.
     */
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }
}
