package com.estagio.backend.service;

import com.estagio.backend.model.Ponto;
import com.estagio.backend.repository.PontoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PontoService {

    private final PontoRepository repository;

    public PontoService(PontoRepository repository) {
        this.repository = repository;
    }

    public List<Ponto> listarTodos() {
        return repository.findAll();
    }

    public Optional<Ponto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Ponto> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId);
    }

    public Ponto salvar(Ponto ponto) {
        // Regra de negócio: a saída não pode ser registrada antes da entrada
        if (ponto.getSaida() != null && ponto.getInicio() != null) {
            if (ponto.getSaida().isBefore(ponto.getInicio())) {
                throw new IllegalArgumentException("O horário de saída não pode ser anterior ao horário de início.");
            }
        }
        return repository.save(ponto);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Registro de ponto com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}