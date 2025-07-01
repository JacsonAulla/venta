package com.mycompany.proyecto.usuarios;

import com.mycompany.proyecto.seguridad.Autenticacion;

public class Empleado extends Usuario {
    public Empleado(int id, String nombre, String correo, String password) {
        super(id, nombre, correo, password, "empleado");
    }

    @Override
    public boolean validarCredenciales(String correo, String password) {
        return Autenticacion.validarCredenciales(correo, password, this);
    }

    public void prepararEnvios() {
        System.out.println("Preparando envíos...");
    }

    public void consultarPedidos() {
        System.out.println("Consultando pedidos...");
    }

    public void consultarInventario() {
        System.out.println("Inventario consultado por empleado.");
    }
}
