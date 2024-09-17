package com.rhb.digital.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import com.rhb.digital.service.ExternalApiService;

/** When we are calling any API usually SSL certs required to be imported
 into keystore that keystore has to be imported to $JAVA_HOME/lib/security/cacerts Or certificate import into custom keystore which is configured in application.properties or application.yml file for respective microservice**/
@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    private final WebClient webClient;

    public ExternalApiServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.rhbgroup.com").build();
    }

    // Synchronous call
    @Override
    public String callExternalApi() {
        try {
            String responseBody = webClient.get()
                    .uri("index.html")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), response -> {
                        return Mono.error(new RuntimeException("Client error: " + response.statusCode()));
                    })
                    .onStatus(status -> status.is5xxServerError(), response -> {
                        return Mono.error(new RuntimeException("Server error: " + response.statusCode()));
                    })
                    .bodyToMono(String.class)
                    .block();

            return responseBody;

        } catch (WebClientResponseException e) {
            return "Error occurred: " + e.getMessage();
        } catch (Exception e) {
            return "Error occurred while calling external API";
        }
    }

    //Asynchronous call
    @Override
    public Mono<String> callExternalApiAsync() {
        return webClient.get()
                .uri("/overview/insurance/index.html")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> processResponseAsync(response)) // Asynchronously process the response
                .onErrorResume(error -> {
                    System.err.println("Fallback triggered due to: " + error.getMessage());
                    return Mono.just("Fallback result due to error");
                });
    }

    private Mono<String> processResponseAsync(String response) {
        return Mono.just("Processed: " + response);
    }
}