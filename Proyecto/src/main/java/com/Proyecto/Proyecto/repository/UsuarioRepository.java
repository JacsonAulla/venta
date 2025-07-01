package com.Proyecto.Proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Proyecto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}

