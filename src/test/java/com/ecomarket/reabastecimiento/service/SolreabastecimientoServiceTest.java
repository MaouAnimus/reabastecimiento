package com.ecomarket.reabastecimiento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.reabastecimiento.enums.EstadoSolicitud;
import com.ecomarket.reabastecimiento.model.EncargadoDTO;
import com.ecomarket.reabastecimiento.model.Producto;
import com.ecomarket.reabastecimiento.model.ProveedorDTO;
import com.ecomarket.reabastecimiento.model.SolicitudProducto;
import com.ecomarket.reabastecimiento.model.Solreabastecimiento;
import com.ecomarket.reabastecimiento.repository.SolreabastecimientoRepository;

public class SolreabastecimientoServiceTest {
    @Mock
    private SolreabastecimientoRepository solReabastecimientoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SolreabastecimientoService solreabastecimientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearSolicitud() {
        ProveedorDTO proveedor = new ProveedorDTO(1L, "Nuevo Proveedor");
        EncargadoDTO encargado = new EncargadoDTO(1L, "Alva", "ro@Duocuc.cl");
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);

        Solreabastecimiento solicitudGuardada = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        
        when(restTemplate.getForObject(any(String.class), eq(EncargadoDTO.class))).thenReturn(encargado);
        when(restTemplate.getForObject(any(String.class), eq(ProveedorDTO.class))).thenReturn(proveedor);
        when(restTemplate.getForObject(any(String.class), eq(Producto.class))).thenReturn(producto);

        when(solReabastecimientoRepository.save(any(Solreabastecimiento.class))).thenReturn(solicitudGuardada);
        Solreabastecimiento resultado = solreabastecimientoService.crearSolicitud(solicitud);

        assertNotNull(resultado);
        assertEquals(solicitudGuardada.getIdSolicitud(), resultado.getIdSolicitud());
        assertEquals(solicitudGuardada.getDireccionEntrega(), resultado.getDireccionEntrega());
        assertEquals(solicitudGuardada.getEstadoS(), resultado.getEstadoS());
        assertEquals(solicitudGuardada.getIdEncargado(), resultado.getIdEncargado());
        assertEquals(solicitudGuardada.getEmail(), resultado.getEmail());
        assertEquals(solicitudGuardada.getIdProveedor(), resultado.getIdProveedor());
        assertEquals(solicitudGuardada.getNombreProveedor(), resultado.getNombreProveedor());
        assertEquals(1, resultado.getProductosSolicitados().size());
        
        verify(restTemplate).getForObject(any(String.class), eq(EncargadoDTO.class));
        verify(restTemplate).getForObject(any(String.class), eq(ProveedorDTO.class));
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(Producto.class));

        verify(solReabastecimientoRepository).save(any(Solreabastecimiento.class));
    }

    @Test
    void testListar() {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);
        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados);
        Solreabastecimiento solicitud2 = new Solreabastecimiento(1L, "Direccion2", EstadoSolicitud.PENDIENTE, 1L, "ro@Duoc.cl", "Al", 1L, "Nuevo Proveedor2", productosSolicitados);
        when(solReabastecimientoRepository.findAll()).thenReturn(Arrays.asList(solicitud, solicitud2));

        when(restTemplate.getForObject(any(String.class), eq(Producto.class))).thenReturn(producto);
        List<Solreabastecimiento> resultado = solreabastecimientoService.findAll();
        assertThat(resultado).hasSize(2).contains(solicitud, solicitud2);
        verify(solReabastecimientoRepository).findAll();
        }
        


    @Test
    void testFindById() {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");
        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados );
        
        when(solReabastecimientoRepository.findById(1L)).thenReturn(java.util.Optional.of(solicitud));
        Solreabastecimiento resultado = solreabastecimientoService.findById(1L);
        assertThat(resultado).isEqualTo(solicitud);
        verify(solReabastecimientoRepository).findById(1L);
    }

    @Test
    void testUpdateSolicitud() {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");
        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);
        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados );
        Solreabastecimiento solicitudUpd = new Solreabastecimiento(1L, "DireccionNueva", EstadoSolicitud.AUTORIZADA, 1L, "ro@uc.cl", "Al", 1L, "NuevProveedor", productosSolicitados );
        
        when(solReabastecimientoRepository.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solReabastecimientoRepository.save(any(Solreabastecimiento.class))).thenReturn(solicitud);
        Solreabastecimiento resultado = solreabastecimientoService.updateSolicitud(1L, solicitudUpd);
        assertEquals("DireccionNueva", resultado.getDireccionEntrega());
        assertEquals(EstadoSolicitud.AUTORIZADA, resultado.getEstadoS());
        verify(solReabastecimientoRepository).findById(1L);
        verify(solReabastecimientoRepository).save(solicitud);
    }   

    @Test
    void testUpdateSolicitudNotFound() {
        Producto producto = new Producto(1L, "ProductoTest", "TEST321");
        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(producto.getIdProducto());
        solProducto.setCantidad(20);
        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento(1L, "Direccion", EstadoSolicitud.PENDIENTE, 1L, "ro@Duocuc.cl", "Alva", 1L, "Nuevo Proveedor", productosSolicitados );
        when(solReabastecimientoRepository.findById(1L)).thenReturn(Optional.empty());
        Solreabastecimiento resultado = solreabastecimientoService.updateSolicitud(1L, solicitud);
        assertEquals(null, resultado);
        verify(solReabastecimientoRepository).findById(1L);
    }
    @Test
    void testFindAllEmpty() {
        when(solReabastecimientoRepository.findAll()).thenReturn(new ArrayList<>());
        List<Solreabastecimiento> resultado = solreabastecimientoService.findAll();
        assertThat(resultado).isEmpty();
        verify(solReabastecimientoRepository).findAll();
    }
    
    @Test
    void testFindByIdNotFound() {
        when(solReabastecimientoRepository.findById(1L)).thenReturn(Optional.empty());
        Solreabastecimiento resultado = solreabastecimientoService.findById(1L);
        assertThat(resultado).isNull();
        verify(solReabastecimientoRepository).findById(1L);
    }

    

    @Test
    void testCrearSolicitud_EncargadoNotFound() {
        Solreabastecimiento solicitud = new Solreabastecimiento();
        solicitud.setIdEncargado(1L);
        solicitud.setIdProveedor(1L);
        solicitud.setProductosSolicitados(new ArrayList<>());

        when(restTemplate.getForObject(any(String.class), eq(EncargadoDTO.class))).thenReturn(null);

        RuntimeException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> solreabastecimientoService.crearSolicitud(solicitud));
        assertThat(thrown.getMessage()).contains("Encargado no encontrado");
    }

    @Test
    void testCrearSolicitud_SinProductos() {
        ProveedorDTO proveedor = new ProveedorDTO(1L, "Proveedor");
        EncargadoDTO encargado = new EncargadoDTO(1L, "Alva", "ro@Duocuc.cl");

        Solreabastecimiento solicitud = new Solreabastecimiento();
        solicitud.setIdEncargado(1L);
        solicitud.setIdProveedor(1L);
        solicitud.setProductosSolicitados(null); // <- lista nula

        when(restTemplate.getForObject(any(String.class), eq(EncargadoDTO.class))).thenReturn(encargado);
        when(restTemplate.getForObject(any(String.class), eq(ProveedorDTO.class))).thenReturn(proveedor);
        when(solReabastecimientoRepository.save(any(Solreabastecimiento.class))).thenReturn(solicitud);

        Solreabastecimiento resultado = solreabastecimientoService.crearSolicitud(solicitud);

        assertNotNull(resultado);
        verify(solReabastecimientoRepository).save(solicitud);
    }

    @Test
    void testCrearSolicitud_ProveedorNotFound() {
        Solreabastecimiento solicitud = new Solreabastecimiento();
        solicitud.setIdEncargado(1L);
        solicitud.setIdProveedor(1L);
        solicitud.setProductosSolicitados(new ArrayList<>());

        EncargadoDTO encargado = new EncargadoDTO(1L, "Alva", "ro@Duocuc.cl");

        when(restTemplate.getForObject(any(String.class), eq(EncargadoDTO.class))).thenReturn(encargado);
        when(restTemplate.getForObject(any(String.class), eq(ProveedorDTO.class))).thenReturn(null);

        RuntimeException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class, () -> solreabastecimientoService.crearSolicitud(solicitud));

        assertThat(thrown.getMessage()).contains("Proveedor no encontrado");
    }

    @Test
    void testCrearSolicitud_ProductoNotFound() {
        ProveedorDTO proveedor = new ProveedorDTO(1L, "Proveedor");
        EncargadoDTO encargado = new EncargadoDTO(1L, "Alva", "ro@Duocuc.cl");

        SolicitudProducto solProducto = new SolicitudProducto();
        solProducto.setIdProducto(1L);
        solProducto.setCantidad(10);

        List<SolicitudProducto> productosSolicitados = new ArrayList<>();
        productosSolicitados.add(solProducto);

        Solreabastecimiento solicitud = new Solreabastecimiento();
        solicitud.setIdEncargado(1L);
        solicitud.setIdProveedor(1L);
        solicitud.setProductosSolicitados(productosSolicitados);

        when(restTemplate.getForObject(any(String.class), eq(EncargadoDTO.class))).thenReturn(encargado);
        when(restTemplate.getForObject(any(String.class), eq(ProveedorDTO.class))).thenReturn(proveedor);
        when(restTemplate.getForObject(any(String.class), eq(Producto.class))).thenReturn(null); // Producto no encontrado

        RuntimeException thrown = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,() -> solreabastecimientoService.crearSolicitud(solicitud));

        assertThat(thrown.getMessage()).contains("Producto no encontrado");
    }
    
}
