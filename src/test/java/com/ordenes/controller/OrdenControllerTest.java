package com.ordenes.controller;

import com.ordenes.dto.OrdenRequest;
import com.ordenes.dto.OrdenResponse;
import com.ordenes.service.OrdenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

class OrdenControllerTest {

    private final OrdenService ordenService = Mockito.mock(OrdenService.class);

    private final WebTestClient webTestClient =
            WebTestClient.bindToController(new OrdenController(ordenService)).build();

    @Test
    void deberiaCrearOrden() {

        OrdenRequest request = new OrdenRequest("Laptop", 900.0);

        OrdenResponse response =
                new OrdenResponse(1L, "Laptop", 900.0, "PAID");

        Mockito.when(ordenService.crearOrden(request))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/ordenes")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.estado").isEqualTo("PAID");
    }
}