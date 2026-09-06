package com.estagio.backend.service;

import com.estagio.backend.model.PessoaTipo;
import com.estagio.backend.repository.PessoaTipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaTipoService {

    private final PessoaTipoRepository repository;

    // Injeção de dependência por construtor: o Spring entrega o repositório pronto para uso
    public PessoaTipoService(PessoaTipoRepository repository) {
        this.repository = repository;
    }

    public List<PessoaTipo> listarTodos() {
        return repository.findAll();
    }

    public Optional<PessoaTipo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public PessoaTipo salvar(PessoaTipo tipo) {
        // Regra de negócio: não permitir cadastrar dois tipos com nomes idênticos
        Optional<PessoaTipo> existente = repository.findByNome(tipo.getNome());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe um tipo cadastrado com este nome: " + tipo.getNome());
        }
        return repository.save(tipo);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tipo com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}