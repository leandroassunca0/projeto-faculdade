package com.estagio.backend.repository;

import com.estagio.backend.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    // Spring monta: "SELECT * FROM tb_pessoas WHERE cpf = ?"
    Optional<Pessoa> findByCpf(String cpf);
}