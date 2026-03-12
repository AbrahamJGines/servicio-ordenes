package com.ordenes.controller;

import com.ordenes.dto.OrdenRequest;
import com.ordenes.dto.OrdenResponse;
import com.ordenes.service.OrdenService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @PostMapping
    public Mono<OrdenResponse> crearOrden(@RequestBody OrdenRequest request) {
        return ordenService.crearOrden(request);
    }

    @GetMapping
    public Flux<OrdenResponse> listarOrdenes() {
        return ordenService.listarOrdenes();
    }

    @GetMapping("/{id}")
    public Mono<OrdenResponse> obtenerOrden(@PathVariable Long id) {
        return ordenService.obtenerOrden(id);
    }

}
