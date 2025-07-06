package com.ecomarket.reabastecimiento.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecomarket.reabastecimiento.enums.EstadoSolicitud;
import com.ecomarket.reabastecimiento.model.Producto;
import com.ecomarket.reabastecimiento.model.SolicitudProducto;
import com.ecomarket.reabastecimiento.model.Solreabastecimiento;
import com.ecomarket.reabastecimiento.service.SolreabastecimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(SolreabastecimientoController.class)
public class SolreabastecimientoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SolreabastecimientoService solreabastecimientoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearSolicitud() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");
        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);
        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        Mockito.when(solreabastecimientoService.crearSolicitud(Mockito.any(Solreabastecimiento.class))).thenReturn(solicitud);
        
        mockMvc.perform(post("/api/resupply/generar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.direccionEntrega").value("Direccion"))
                .andExpect(jsonPath("$.estadoS").value("PENDIENTE"))
                .andExpect(jsonPath("$.email").value("ro@Duocuc.cl"))
                .andExpect(jsonPath("$.nombre").value("Alva"))
                .andExpect(jsonPath("$.nombreProveedor").value("Nuevo Proveedor"))
                .andExpect(jsonPath("$.productosSolicitados[0].idProducto").value(producto.getIdProducto()))
                .andExpect(jsonPath("$.productosSolicitados[0].cantidad").value(20));
    }
    @Test
    void testListarSolicitudes() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);
        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        Solreabastecimiento solicitud2 = new Solreabastecimiento(1L, "Direccion2", EstadoSolicitud.PENDIENTE, 1L, "ro@Duoc.cl", "Al", 1L, "Nuevo Proveedor2", productosSolicitados);
        
        Mockito.when(solreabastecimientoService.findAll()).thenReturn(Arrays.asList(solicitud, solicitud2));
        mockMvc.perform(get("/api/resupply"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].direccionEntrega").value("Direccion"))
                .andExpect(jsonPath("$[1].direccionEntrega").value("Direccion2"));
    }

    @Test
    void testObtenerSolicitudPorId() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);
        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        Mockito.when(solreabastecimientoService.findById(1L)).thenReturn(solicitud);
        mockMvc.perform(get("/api/resupply/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccionEntrega").value("Direccion"))
                .andExpect(jsonPath("$.estadoS").value("PENDIENTE"))
                .andExpect(jsonPath("$.email").value("ro@Duocuc.cl"))
                .andExpect(jsonPath("$.nombre").value("Alva"))
                .andExpect(jsonPath("$.nombreProveedor").value("Nuevo Proveedor"))
                .andExpect(jsonPath("$.productosSolicitados[0].idProducto").value(producto.getIdProducto()))
                .andExpect(jsonPath("$.productosSolicitados[0].cantidad").value(20));
    }

    @Test
    void testActualizarPorId() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");
        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);
        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados );
        Solreabastecimiento solicitudUpdate = new Solreabastecimiento(1L, "DireccionNueva", EstadoSolicitud.AUTORIZADA, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados );
        Mockito.when(solreabastecimientoService.updateSolicitud(Mockito.eq(1L), Mockito.any(Solreabastecimiento.class))).thenReturn(solicitudUpdate);
        mockMvc.perform(put("/api/resupply/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.direccionEntrega").value("DireccionNueva"))
                .andExpect(jsonPath("$.estadoS").value("AUTORIZADA"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        Mockito.when(solreabastecimientoService.findById(9999999L)).thenReturn(null);
        mockMvc.perform(get("/api/resupply/9999999")).andExpect(status().isNotFound()); 
    }

    @Test
    void testGetAllNoContent() throws Exception {
        when(solreabastecimientoService.findAll()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/resupply"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testCrearSolicitudError() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);
        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        
        Mockito.when(solreabastecimientoService.crearSolicitud(Mockito.any(Solreabastecimiento.class))).thenThrow(new RuntimeException("Error al crear solicitud"));

        mockMvc.perform(post("/api/resupply/generar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(solicitud)))
            .andExpect(status().isConflict());
    }

    @Test
    void testActualizarSolicitudNotFound() throws Exception {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);
        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        Mockito.when(solreabastecimientoService.updateSolicitud(
        Mockito.eq(1L), Mockito.any(Solreabastecimiento.class))).thenReturn(null);

        mockMvc.perform(put("/api/resupply/actualizar/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(solicitud)))
            .andExpect(status().isNotFound());
    }
}
