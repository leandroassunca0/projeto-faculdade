package com.estagio.backend.service;

import com.estagio.backend.model.Pessoa;
import com.estagio.backend.model.PessoaTipo;
import com.estagio.backend.repository.PessoaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository repository;

    @InjectMocks
    private PessoaService service;

    @Test
    @DisplayName("Deve salvar uma pessoa quando o CPF for inédito")
    void deveSalvarPessoaComSucesso() {
        PessoaTipo tipo = new PessoaTipo("Estagiário");
        Pessoa pessoa = new Pessoa("Ana Silva", "123.456.789-00", LocalDate.of(2000, 1, 1), "11999998888", tipo);

        when(repository.findByCpf("123.456.789-00")).thenReturn(Optional.empty());
        when(repository.save(pessoa)).thenReturn(pessoa);

        Pessoa salva = service.salvar(pessoa);

        assertNotNull(salva);
        assertEquals("Ana Silva", salva.getNome());
        assertEquals("123.456.789-00", salva.getCpf());
        verify(repository, times(1)).save(pessoa);
    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar pessoa com CPF duplicado")
    void deveRejeitarCpfDuplicado() {
        PessoaTipo tipo = new PessoaTipo("Estagiário");
        Pessoa existente = new Pessoa("Ana Silva", "123.456.789-00", LocalDate.of(2000, 1, 1), "11999998888", tipo);

        when(repository.findByCpf("123.456.789-00")).thenReturn(Optional.of(existente));

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(new Pessoa("Carlos Souza", "123.456.789-00", LocalDate.of(1995, 5, 20), "11888887777", tipo));
        });

        assertEquals("Já existe uma pessoa cadastrada com o CPF: 123.456.789-00", erro.getMessage());
        verify(repository, never()).save(any());
    }
}