package com.mycompany.proyecto.modelo;

import com.mycompany.proyecto.usuarios.Cliente;
import java.util.Date;

public class SolicitudCompra {
    private int id;
    private Date fecha;
    private String estado;
    private Producto producto;
    private Cliente cliente;

    public SolicitudCompra(int id, Date fecha, Producto producto, Cliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.estado = "pendiente";
        this.producto = producto;
        this.cliente = cliente;
    }

    public void confirmar() {
        this.estado = "confirmado";
    }

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public Producto getProducto() {
        return producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
