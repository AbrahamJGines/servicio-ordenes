package com.ordenes.dto;

public record OrdenResponse(
        Long id,
        String producto,
        Double monto,
        String estado
) {}
