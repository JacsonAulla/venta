package com.mycompany.proyecto.usuarios;

import com.mycompany.proyecto.seguridad.Autenticacion;
import com.mycompany.proyecto.modelo.Producto;

public class Administrador extends Usuario {
    public Administrador(int id, String nombre, String correo, String password) {
        super(id, nombre, correo, password, "admin");
    }

    @Override
    public boolean validarCredenciales(String correo, String password) {
        return Autenticacion.validarCredenciales(correo, password, this);
    }

    public void crearUsuario(Usuario usuario) {
        System.out.println("Usuario creado: " + usuario.getNombre());
    }

    public void modificarUsuario(Usuario usuario) {
        System.out.println("Usuario modificado: " + usuario.getNombre());
    }

    public void eliminarUsuario(Usuario usuario) {
        System.out.println("Usuario eliminado: " + usuario.getNombre());
    }

    public void gestionarRoles() {
        System.out.println("Roles gestionados.");
    }

    public void modificarProducto(Producto producto) {
        System.out.println("Producto modificado: " + producto.getNombre());
    }
}
