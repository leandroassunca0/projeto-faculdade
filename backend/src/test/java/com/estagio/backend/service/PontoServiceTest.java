package com.estagio.backend.service;

import com.estagio.backend.model.Pessoa;
import com.estagio.backend.model.Ponto;
import com.estagio.backend.repository.PontoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PontoServiceTest {

    @Mock
    private PontoRepository repository;

    @InjectMocks
    private PontoService service;

    @Test
    @DisplayName("Deve salvar ponto com horários válidos")
    void deveSalvarPontoValido() {
        Pessoa funcionario = new Pessoa();
        funcionario.setId(1L);

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 6, 8, 0);
        LocalDateTime saida = LocalDateTime.of(2026, 9, 6, 17, 0);

        Ponto ponto = new Ponto(inicio, saida, "Jornada regular", funcionario);

        when(repository.save(ponto)).thenReturn(ponto);

        Ponto salvo = service.salvar(ponto);

        assertNotNull(salvo);
        assertEquals("Jornada regular", salvo.getJustificativa());
        verify(repository, times(1)).save(ponto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a saída for anterior ao início")
    void deveLancarExcecaoQuandoSaidaForAnteriorAoInicio() {
        Pessoa funcionario = new Pessoa();
        funcionario.setId(1L);

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 6, 17, 0);
        LocalDateTime saidaInvalida = LocalDateTime.of(2026, 9, 6, 8, 0); // 8h é antes das 17h

        Ponto ponto = new Ponto(inicio, saidaInvalida, "Horário incorreto", funcionario);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(ponto);
        });

        assertEquals("O horário de saída não pode ser anterior ao horário de início.", erro.getMessage());
        verify(repository, never()).save(any());
    }
}
