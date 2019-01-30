package com.cumulocity.sdk.mqtt.listener;

import com.cumulocity.sdk.mqtt.model.MqttMessageResponse;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class MqttMessageListener implements IMqttMessageListener {

    private static final String MSG_DELIMITER = ",";

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String mqttMessage = new String(message.getPayload());
        final String clientId = mqttMessage.split(MSG_DELIMITER)[1];
        final MqttMessageResponse messageResponse = MqttMessageResponse.builder().topicName(topic)
                                                    .clientId(clientId)
                                                        .qos(message.getQos())
                                                            .messageContent(mqttMessage)
                                                                .build();
        messageArrived(messageResponse);
    }

    public abstract void messageArrived(MqttMessageResponse messageResponse);
}
