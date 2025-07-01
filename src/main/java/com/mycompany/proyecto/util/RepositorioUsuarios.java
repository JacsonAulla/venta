package com.mycompany.proyecto.util;

import com.mycompany.proyecto.usuarios.*;
import com.mycompany.proyecto.seguridad.Autenticacion;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarios {

    private static List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Cliente(1, "Juan Perez", "juan@correo.com", Autenticacion.cifrarPassword("1234")));
        usuarios.add(new Empleado(2, "Ana Lopez", "ana@correo.com", Autenticacion.cifrarPassword("abcd")));
        usuarios.add(new Administrador(3, "Carlos Ruiz", "carlos@correo.com", Autenticacion.cifrarPassword("admin")));
    }

    public static List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public static void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public static void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public static Usuario buscarPorCorreo(String correo) {
        return usuarios.stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
    }
}
