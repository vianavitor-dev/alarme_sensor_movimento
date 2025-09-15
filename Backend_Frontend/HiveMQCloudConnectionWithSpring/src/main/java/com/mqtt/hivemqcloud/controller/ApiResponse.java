package com.mqtt.hivemqcloud.controller;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class ApiResponse {
    boolean error;
    String message;
    LocalDateTime receivedAt;

    public boolean isOk() {
        return !error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ApiResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public ApiResponse(boolean error, String message, LocalDateTime receivedAt) {
        this.error = error;
        this.message = message;
        this.receivedAt = receivedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public ApiResponse setReceivedAt(Instant now) {
        ZonedDateTime zDateTime = ZonedDateTime.ofInstant(now, ZoneId.of("America/Sao_Paulo"));

        this.receivedAt = zDateTime.toLocalDateTime();
        return this;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "["+ getReceivedAt().format(formatter) +"] " + getMessage();
    }
}
