package com.mycompany.proyecto.usuarios;

import com.mycompany.proyecto.seguridad.Autenticacion;
import com.mycompany.proyecto.modelo.SolicitudCompra;

public class Cliente extends Usuario {
    public Cliente(int id, String nombre, String correo, String password) {
        super(id, nombre, correo, password, "cliente");
    }

    @Override
    public boolean validarCredenciales(String correo, String password) {
        return Autenticacion.validarCredenciales(correo, password, this);
    }

    public void registrarSolicitudCompra(SolicitudCompra solicitud) {
        System.out.println("Solicitud de compra registrada para el cliente: " + nombre);
    }

    public void consultarInventario() {
        System.out.println("Inventario consultado por cliente.");
    }
}
