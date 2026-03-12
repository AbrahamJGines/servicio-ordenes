package com.ordenes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ordenes")
public class Orden {

    @Id
    private Long id;
    private String producto;
    private Double monto;
    private String estado;

    public Orden() {}

    public Orden(Long id, String producto, Double monto, String estado) {
        this.id = id;
        this.producto = producto;
        this.monto = monto;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }

    public Double getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}