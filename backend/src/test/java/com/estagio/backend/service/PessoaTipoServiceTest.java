package com.estagio.backend.service;

import com.estagio.backend.model.PessoaTipo;
import com.estagio.backend.repository.PessoaTipoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaTipoServiceTest {

    @Mock
    private PessoaTipoRepository repository; // Cria um banco "de mentira" para o teste

    @InjectMocks
    private PessoaTipoService service; // Injeta o repositório falso no serviço real

    @Test
    @DisplayName("Deve salvar tipo com sucesso quando o nome for inédito")
    void deveSalvarTipoComSucesso() {
        // Cenário (Arrange)
        PessoaTipo tipo = new PessoaTipo("Tercerizado");
        when(repository.findByNome("Tercerizado")).thenReturn(Optional.empty());
        when(repository.save(tipo)).thenReturn(tipo);

        // Ação (Act)
        PessoaTipo resultado = service.salvar(tipo);

        // Verificação (Assert)
        assertNotNull(resultado);
        assertEquals("Tercerizado", resultado.getNome());
        verify(repository, times(1)).save(tipo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar tipo com nome já existente")
    void deveLancarExcecaoQuandoNomeJaExiste() {
        // Cenário (Arrange): simula que o banco já tem "CLT"
        PessoaTipo tipoExistente = new PessoaTipo("CLT");
        when(repository.findByNome("CLT")).thenReturn(Optional.of(tipoExistente));

        // Ação e Verificação (Act & Assert)
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(new PessoaTipo("CLT"));
        });

        assertEquals("Já existe um tipo cadastrado com este nome: CLT", erro.getMessage());
        verify(repository, never()).save(any()); // Garante que o método save NUNCA foi chamado
    }
}