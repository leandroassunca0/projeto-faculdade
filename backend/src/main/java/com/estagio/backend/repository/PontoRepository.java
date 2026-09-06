package com.estagio.backend.repository;

import com.estagio.backend.model.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {

    // Spring monta: "SELECT * FROM tb_ponto WHERE funcionario_id = ?"
    List<Ponto> findByFuncionarioId(Long funcionarioId);
}