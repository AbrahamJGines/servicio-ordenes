package com.ordenes.dto;

public record OrdenRequest(
        String producto,
        Double monto
) {}
