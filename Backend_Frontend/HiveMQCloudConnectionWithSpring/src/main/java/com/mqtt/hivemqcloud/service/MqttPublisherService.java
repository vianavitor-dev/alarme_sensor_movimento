package com.mqtt.hivemqcloud.service;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.springframework.stereotype.Service;

@Service
public class MqttPublisherService {
    private final Mqtt3AsyncClient mqttClient;

    public MqttPublisherService(Mqtt3AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish(String topic, String message) {
        mqttClient.publishWith()
                .topic(topic)
                .payload(message.getBytes())
                .qos(MqttQos.AT_LEAST_ONCE)
                .send()
                .whenComplete((mqtt3Publish, throwable) -> {
                    if (throwable != null) {
                        System.err.println("Falha ao publicar mensagem: "
                                + throwable.getMessage());
                    }

                    System.out.println("Mensagem publicada: " + message);
                });

    }
}
