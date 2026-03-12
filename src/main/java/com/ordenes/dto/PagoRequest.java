package com.ordenes.dto;

public record PagoRequest(
        Long ordenId,
        Double monto
) {}
