package com.estagio.backend.service;

import com.estagio.backend.model.Pessoa;
import com.estagio.backend.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<Pessoa> listarTodas() {
        return repository.findAll();
    }

    public Optional<Pessoa> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Pessoa salvar(Pessoa pessoa) {
        // Regra de negócio: não permitir cadastrar pessoas com o mesmo CPF
        Optional<Pessoa> existente = repository.findByCpf(pessoa.getCpf());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        return repository.save(pessoa);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Pessoa com ID " + id + " não encontrada.");
        }
        repository.deleteById(id);
    }
}