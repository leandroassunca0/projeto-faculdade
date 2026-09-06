package com.estagio.backend.service;

import com.estagio.backend.model.Usuario;
import com.estagio.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        // Regra de negócio: não permitir dois usuários com o mesmo login
        Optional<Usuario> existente = repository.findByLogin(usuario.getLogin());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com este login: " + usuario.getLogin());
        }
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Usuário com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}