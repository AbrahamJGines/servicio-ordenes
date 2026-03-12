package com.ordenes.client;

import com.ordenes.dto.PagoRequest;
import com.ordenes.dto.PagoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PagoClient {

    private final WebClient webClient;

    public PagoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PagoResponse> procesarPago(PagoRequest request) {

        return webClient.post()
                .uri("/pagos")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PagoResponse.class);
    }

}
