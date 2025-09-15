package com.mqtt.hivemqcloud.controller;

import com.mqtt.hivemqcloud.helper.TxtHelper;
import com.mqtt.hivemqcloud.service.HistoryService;
import com.mqtt.hivemqcloud.service.MqttPublisherService;
import com.mqtt.hivemqcloud.service.MqttSubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/mqtt")
public class MqttController {
    private MqttPublisherService publisher;
    private MqttSubscriberService subscriber;
    private HistoryService historyService;

    public MqttController(MqttPublisherService publisher, MqttSubscriberService subscriber, HistoryService historyService) {
        this.publisher = publisher;
        this.subscriber = subscriber;
        this.historyService = historyService;
    }

    @PostMapping("/publish")
    public ResponseEntity<ApiResponse> publish(@RequestParam String topic, @RequestParam String message) {
        publisher.publish(topic, message);

        return new ResponseEntity<>(
                new ApiResponse(
                        false, "Mensagem publicada" + message
                ).setReceivedAt(Instant.now()),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> subscribe() {
        subscriber.subscribe();

        ApiResponse response = new ApiResponse(false, subscriber.getMessage())
                .setReceivedAt(Instant.now());

        historyService.saveIntoHistory(response);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}
