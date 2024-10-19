package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.entity.Orden;
import com.example.ecommerce.entity.Usuario;
import com.example.ecommerce.repository.OrdenRepository;
import com.example.ecommerce.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class OrdenServiceTest {
    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearOrden_DeberiaCrearOrdenExitosamente() {
        // Arrange
        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setUsuarioId(1L);
        ordenDTO.setDetalles(new ArrayList<>());
        ordenDTO.setFecha(LocalDateTime.now());
        ordenDTO.setTotal(new BigDecimal("100.00"));
        ordenDTO.setDescuento(BigDecimal.ZERO);
        ordenDTO.setEstado("PENDIENTE");

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Orden ordenGuardada = new Orden();
        ordenGuardada.setId(1L);
        ordenGuardada.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ordenRepository.save(any(Orden.class))).thenReturn(ordenGuardada);

        // Act
        OrdenDTO resultado = ordenService.crearOrden(ordenDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }
}
