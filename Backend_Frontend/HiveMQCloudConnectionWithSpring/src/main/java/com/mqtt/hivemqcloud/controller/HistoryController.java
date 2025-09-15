package com.mqtt.hivemqcloud.controller;

import com.mqtt.hivemqcloud.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/history")
public class HistoryController {
    @Autowired
    private HistoryService service;

    @GetMapping
    public ResponseEntity<String> listHistory() {
        return new ResponseEntity<>(
                service.getHistory(),
                HttpStatus.OK
        );
    }
}
