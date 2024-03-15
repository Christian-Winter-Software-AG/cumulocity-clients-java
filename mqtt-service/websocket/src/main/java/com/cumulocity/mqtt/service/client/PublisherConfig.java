package com.cumulocity.mqtt.service.client;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * {@link PublisherConfig} provides configuration for {@link MqttPublisher}
 */
@ToString(exclude = {"connectionListener"})
@FieldDefaults(makeFinal = true, level = PRIVATE)
@EqualsAndHashCode(exclude = "connectionListener")
public class PublisherConfig {

    String id;

    String topic;

    ConnectionListener connectionListener;

    @Builder(builderMethodName = "publisherConfig")
    private PublisherConfig(final String id, final String topic, final ConnectionListener connectionListener) {
        this.id = isBlank(id) ? String.format("publisher:%s", topic) : id;
        this.topic = topic;
        this.connectionListener = isNull(connectionListener) ? new LoggingConnectionListener() : connectionListener;
    }

    /**
     * @return unique publisher id used to identify the instance of {@link MqttPublisher} in the {@link MqttClient} and {@link ConnectionListener}
     */
    public String getId() {
        return id;
    }

    /**
     * @return the topic to which instance of {@link MqttPublisher} will connect to.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @return connection listener which allows to monitor established connection.
     */
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }
}
