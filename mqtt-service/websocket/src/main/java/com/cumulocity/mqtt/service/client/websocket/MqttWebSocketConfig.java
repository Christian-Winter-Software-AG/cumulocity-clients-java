package com.cumulocity.mqtt.service.client.websocket;

import com.cumulocity.mqtt.service.client.MqttConfig;
import com.cumulocity.mqtt.service.client.MqttPublisher;
import com.cumulocity.mqtt.service.client.MqttSubscriber;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MqttWebSocketConfig implements MqttConfig {

    private final static long DEFAULT_CONNECTION_TIMEOUT_MILLIS = 2000;

    /**
     * Specify the topic to which instance of {@link MqttPublisher} or {@link MqttSubscriber} will connect to.
     */
    private final String topic;

    /**
     * Specify the subscriber name (consumer name) which will be used by the instance of {@link MqttSubscriber}.
     */
    private final String subscriber;

    /**
     * Connection timeout in millis second until the websocket connected or failed to do so.
     */
    @Builder.Default
    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MILLIS;

    public static class MqttWebSocketConfigBuilder {
    }
}