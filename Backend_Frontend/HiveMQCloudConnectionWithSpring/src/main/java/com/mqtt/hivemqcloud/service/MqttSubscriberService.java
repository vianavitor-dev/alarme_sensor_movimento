package com.mqtt.hivemqcloud.service;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.mqtt.hivemqcloud.controller.ApiResponse;
import com.mqtt.hivemqcloud.helper.TxtHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MqttSubscriberService {
    Mqtt3AsyncClient mqttClient;
    private static String message;
    private static boolean newMessage;
    private static String lastMessage;

    public MqttSubscriberService(Mqtt3AsyncClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void subscribe() {
        mqttClient.subscribeWith()
                .topicFilter("home/sensor/movement")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(mqtt3Publish -> {
                    String payload = new String(mqtt3Publish.getPayloadAsBytes());

                    message = "Mensagem '"+ payload +"' recebida no tópico: "
                            + mqtt3Publish.getTopic() + "\n";
                })
                .send()
                .whenComplete((mqtt3SubAck, throwable) -> {
                    if (throwable != null) {
                        System.err.println("Erro ao se increver no tópico: "
                                + throwable.getMessage());
                    }

                    System.out.println("Inscrição no tópico concluida!");
                });

    }

    public String getMessage() {
        return message;
    }

    public static boolean isNewMessage() {
        return newMessage;
    }

    public static void setNewMessage(boolean newMessage) {
        MqttSubscriberService.newMessage = newMessage;
    }

    public static void setMessage(String message) {
        MqttSubscriberService.message = message;
    }
}
