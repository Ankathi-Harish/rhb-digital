package com.rhb.digital.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ExternalApiService {
    String callExternalApi();
    public Mono<String> callExternalApiAsync();
}
