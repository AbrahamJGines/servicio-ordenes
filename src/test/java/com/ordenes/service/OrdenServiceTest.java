package com.ordenes.service;

import com.ordenes.client.PagoClient;
import com.ordenes.dto.*;
import com.ordenes.model.Orden;
import com.ordenes.repository.OrdenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

class OrdenServiceTest {

    private final OrdenRepository ordenRepository = Mockito.mock(OrdenRepository.class);
    private final PagoClient pagoClient = Mockito.mock(PagoClient.class);

    private final OrdenService ordenService =
            new OrdenService(ordenRepository, pagoClient);

    @Test
    void deberiaCrearOrdenPagada() {

        OrdenRequest request = new OrdenRequest("Laptop", 900.0);

        Orden orden = new Orden(1L, "Laptop", 900.0, "CREATED");

        Mockito.when(ordenRepository.save(any()))
                .thenReturn(Mono.just(orden));

        Mockito.when(pagoClient.procesarPago(any()))
                .thenReturn(Mono.just(new PagoResponse("APPROVED")));

        StepVerifier.create(ordenService.crearOrden(request))
                .expectNextMatches(response ->
                        response.estado().equals("PAID"))
                .verifyComplete();
    }
}