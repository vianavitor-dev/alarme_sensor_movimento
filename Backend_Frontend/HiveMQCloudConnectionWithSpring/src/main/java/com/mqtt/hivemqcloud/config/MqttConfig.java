package com.mqtt.hivemqcloud.config;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    private String HOST;
    private int PORT;
    private String USERNAME;
    private String PASSWORD;

    @Bean
    public Mqtt3AsyncClient mqttClient() {
        Mqtt3AsyncClient client = MqttClient.builder()
                .useMqttVersion3()
                .serverHost(HOST)
                .serverPort(PORT)
                .sslWithDefaultConfig()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username(USERNAME)
                .password(PASSWORD.getBytes())
                .applySimpleAuth()
                .send()
                .whenComplete((mqtt3ConnAck, throwable) -> {
                    if (throwable != null) {
                        System.err.println("Falha ao se conectar ao HiveMQ Cloud: "+throwable.getMessage());
                        return;
                    }
                    System.out.println("Conectado com succeso!");
                });

        return client;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
