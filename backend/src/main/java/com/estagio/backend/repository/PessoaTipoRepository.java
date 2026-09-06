package com.estagio.backend.repository;

import com.estagio.backend.model.PessoaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaTipoRepository extends JpaRepository<PessoaTipo, Long> {

    Optional<PessoaTipo> findByNome(String nome);
}