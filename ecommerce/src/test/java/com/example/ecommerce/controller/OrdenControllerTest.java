package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrdenDTO;
import com.example.ecommerce.service.OrdenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearOrden_DeberiaRetornarOrdenCreada() throws Exception {
        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setUsuarioId(1L);
        // Configura más campos de ordenDTO según sea necesario

        OrdenDTO ordenCreada = new OrdenDTO();
        ordenCreada.setId(1L);
        ordenCreada.setUsuarioId(1L);

        when(ordenService.crearOrden(any(OrdenDTO.class))).thenReturn(ordenCreada);

        mockMvc.perform(post("/api/ordenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ordenDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuarioId").value(1L));
    }

}