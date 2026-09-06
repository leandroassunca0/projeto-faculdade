package com.estagio.backend.controller;

import com.estagio.backend.model.PessoaTipo;
import com.estagio.backend.service.PessoaTipoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoa-tipos")
@CrossOrigin(origins = "*") // Permite que o frontend (React) faça chamadas sem ser bloqueado
public class PessoaTipoController {

    private final PessoaTipoService service;

    public PessoaTipoController(PessoaTipoService service) {
        this.service = service;
    }

    // GET /api/pessoa-tipos -> Retorna a lista completa
    @GetMapping
    public List<PessoaTipo> listarTodos() {
        return service.listarTodos();
    }

    // GET /api/pessoa-tipos/{id} -> Retorna um único item pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PessoaTipo> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/pessoa-tipos -> Cadastra um novo registro
    @PostMapping
    public ResponseEntity<PessoaTipo> criar(@RequestBody PessoaTipo tipo) {
        PessoaTipo novoTipo = service.salvar(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
    }

    // DELETE /api/pessoa-tipos/{id} -> Remove um registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}