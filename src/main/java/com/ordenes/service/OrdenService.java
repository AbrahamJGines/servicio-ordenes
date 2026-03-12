package com.ordenes.service;

import com.ordenes.client.PagoClient;
import com.ordenes.dto.OrdenRequest;
import com.ordenes.dto.OrdenResponse;
import com.ordenes.dto.PagoRequest;
import com.ordenes.model.Orden;
import com.ordenes.model.OrdenEstado;
import com.ordenes.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final PagoClient pagoClient;

    public OrdenService(OrdenRepository ordenRepository, PagoClient pagoClient) {
        this.ordenRepository = ordenRepository;
        this.pagoClient = pagoClient;
    }

    public Mono<OrdenResponse> crearOrden(OrdenRequest request) {

        Orden orden = new Orden();
        orden.setProducto(request.producto());
        orden.setMonto(request.monto());
        orden.setEstado(OrdenEstado.CREATED.name());

        return ordenRepository.save(orden)
                .flatMap(savedOrden -> {

                    PagoRequest pagoRequest =
                            new PagoRequest(savedOrden.getId(), savedOrden.getMonto());

                    return pagoClient.procesarPago(pagoRequest)
                            .flatMap(pagoResponse -> {

                                if (pagoResponse.estado().equals("APPROVED")) {
                                    marcarOrdenPagada.accept(savedOrden);
                                } else {
                                    marcarOrdenFallida.accept(savedOrden);
                                }

                                return ordenRepository.save(savedOrden);
                            });
                })
                .map(this::toResponse);
    }

    public Flux<OrdenResponse> listarOrdenes() {
        return ordenRepository.findAll()
                .collectList()
                .map(lista -> lista.stream()
                        .filter(o -> o.getEstado().equals(OrdenEstado.PAID.name()))
                        .toList()
                )
                .flatMapMany(Flux::fromIterable)
                .map(this::toResponse);
    }

    public Mono<OrdenResponse> obtenerOrden(Long id) {

        return ordenRepository.findById(id)
                .map(Optional::ofNullable)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalOrden -> optionalOrden
                        .map(orden -> Mono.just(toResponse(orden)))
                        .orElseGet(() -> Mono.error(new RuntimeException("Orden no encontrada")))
                );
    }

    private OrdenResponse toResponse(Orden orden) {
        return new OrdenResponse(
                orden.getId(),
                orden.getProducto(),
                orden.getMonto(),
                orden.getEstado()
        );
    }

    private final Consumer<Orden> marcarOrdenPagada =
            orden -> orden.setEstado(OrdenEstado.PAID.name());

    private final Consumer<Orden> marcarOrdenFallida =
            orden -> orden.setEstado(OrdenEstado.PAYMENT_FAILED.name());

}
