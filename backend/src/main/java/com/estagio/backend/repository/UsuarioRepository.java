package com.estagio.backend.repository;

import com.estagio.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring monta: "SELECT * FROM tb_usuarios WHERE login = ?"
    Optional<Usuario> findByLogin(String login);
}