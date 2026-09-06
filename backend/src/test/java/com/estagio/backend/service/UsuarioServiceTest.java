package com.estagio.backend.service;

import com.estagio.backend.model.Usuario;
import com.estagio.backend.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("Deve salvar usuário quando o login for inédito")
    void deveSalvarUsuarioComSucesso() {
        Usuario usuario = new Usuario("Admin", "admin", "123456");
        when(repository.findByLogin("admin")).thenReturn(Optional.empty());
        when(repository.save(usuario)).thenReturn(usuario);

        Usuario salvo = service.salvar(usuario);

        assertNotNull(salvo);
        assertEquals("admin", salvo.getLogin());
        verify(repository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar login duplicado")
    void deveRejeitarLoginDuplicado() {
        Usuario existente = new Usuario("Admin", "admin", "123456");
        when(repository.findByLogin("admin")).thenReturn(Optional.of(existente));

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(new Usuario("Outro Admin", "admin", "senha"));
        });

        assertEquals("Já existe um usuário com este login: admin", erro.getMessage());
        verify(repository, never()).save(any());
    }
}