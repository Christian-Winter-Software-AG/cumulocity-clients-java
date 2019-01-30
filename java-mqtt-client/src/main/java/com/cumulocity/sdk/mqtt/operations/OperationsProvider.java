package com.cumulocity.sdk.mqtt.operations;

import com.cumulocity.sdk.mqtt.model.ConnectionDetails;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface OperationsProvider {

    void createConnection(ConnectionDetails connectionDetails) throws MqttException;

    void publish(String topicName, int qos, String payload) throws MqttException;

    void subscribe(String topicName, int qos, IMqttMessageListener messageListener) throws MqttException;

    void disconnect() throws MqttException;

    boolean isConnectionEstablished();
}
