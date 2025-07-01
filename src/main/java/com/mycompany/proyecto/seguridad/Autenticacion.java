package com.mycompany.proyecto.seguridad;

import com.mycompany.proyecto.usuarios.Usuario;

public class Autenticacion {

    public static boolean validarCredenciales(String correo, String password, Usuario usuario) {
        return usuario.getCorreo().equals(correo) &&
           usuario.getPassword().equals(cifrarPassword(password));
    }

    public static String cifrarPassword(String password) {
        // Método básico de cifrado (hash)
        return Integer.toString(password.hashCode());
    }
}
