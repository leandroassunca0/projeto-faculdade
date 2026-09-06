package com.estagio.backend.controller;

import com.estagio.backend.model.Ponto;
import com.estagio.backend.service.PontoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos")
@CrossOrigin(origins = "*")
public class PontoController {

    private final PontoService service;

    public PontoController(PontoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ponto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ponto> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public List<Ponto> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return service.listarPorFuncionario(funcionarioId);
    }

    @PostMapping
    public ResponseEntity<Ponto> criar(@RequestBody Ponto ponto) {
        Ponto novoPonto = service.salvar(ponto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPonto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}