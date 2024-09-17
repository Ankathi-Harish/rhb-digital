package com.rhb.digital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhb.digital.service.ExternalApiService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/external")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/data")
    public ResponseEntity<String> getExternalData() {
        String data = externalApiService.callExternalApi();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/async-data")
    public Mono<String> getAsyncData() {
        return externalApiService.callExternalApiAsync(); // Non-blocking response
    }
}