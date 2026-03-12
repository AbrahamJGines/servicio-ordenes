package com.ordenes.repository;

import com.ordenes.model.Orden;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrdenRepository extends ReactiveCrudRepository<Orden, Long> {
}
