package com.ecomarket.reabastecimiento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecomarket.reabastecimiento.model.EncargadoDTO;
import com.ecomarket.reabastecimiento.model.Producto;
import com.ecomarket.reabastecimiento.model.ProveedorDTO;
import com.ecomarket.reabastecimiento.model.SolicitudProducto;
import com.ecomarket.reabastecimiento.model.Solreabastecimiento;
import com.ecomarket.reabastecimiento.repository.SolreabastecimientoRepository;

@Service
public class SolreabastecimientoService {
    @Autowired
    private SolreabastecimientoRepository solreabastecimientoRepository;

    public Solreabastecimiento crearSolicitud(Solreabastecimiento resupply) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8083/api/usuario/" + resupply.getIdEncargado();
        EncargadoDTO encargado = restTemplate.getForObject(url, EncargadoDTO.class);
        String urlProv = "http://localhost:8084/api/proveedores/" + resupply.getIdProveedor();
        ProveedorDTO proveedor = restTemplate.getForObject(urlProv, ProveedorDTO.class);
        
        if (encargado == null) {
            throw new RuntimeException("Encargado no encontrado con ID: " + resupply.getIdEncargado());
        }
        if (proveedor == null) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + resupply.getIdProveedor());
        }
            resupply.setIdEncargado(encargado.getId());
            resupply.setNombre(encargado.getNombre());
            resupply.setEmail(encargado.getEmail());
            resupply.setIdProveedor(proveedor.getIdProveedor());
            resupply.setNombreProveedor(proveedor.getNombreProveedor());
            if (resupply.getProductosSolicitados() != null) {
                for (SolicitudProducto p : resupply.getProductosSolicitados()) {
                String urlProd = "http://localhost:8086/api/inventario/" + p.getIdProducto();
                Producto producto = restTemplate.getForObject(urlProd, Producto.class);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con ID: " + p.getIdProducto());
            }
            p.setProducto(producto);

        p.setSolicitudReabastecimiento(resupply);
    }
        }
            
            return solreabastecimientoRepository.save(resupply);  
    }

    public Solreabastecimiento findById(Long id_solreabastecimiento) {
        return solreabastecimientoRepository.findById((long) id_solreabastecimiento).orElse(null);    
    }

    public List<Solreabastecimiento> findAll() {
        List<Solreabastecimiento> lista = solreabastecimientoRepository.findAll();
        RestTemplate restTemplate = new RestTemplate();
        for (Solreabastecimiento solicitud : lista) {
            if (solicitud.getProductosSolicitados() != null) {
                for (SolicitudProducto sp : solicitud.getProductosSolicitados()) {
                    String urlProd = "http://localhost:8086/api/inventario/" + sp.getIdProducto();
                    Producto producto = restTemplate.getForObject(urlProd, Producto.class);
                    sp.setProducto(producto); 
            }
        }
    }
        return lista;
}

    public Solreabastecimiento updateSolicitud(Long id_solicitud, Solreabastecimiento resupply) {
        Solreabastecimiento solicitudExistente = solreabastecimientoRepository.findById(id_solicitud).orElse(null);
        if (solicitudExistente != null) {
            if (resupply.getDireccionEntrega() != null) {
                solicitudExistente.setDireccionEntrega(resupply.getDireccionEntrega());
            }
            if (resupply.getEstadoS() != null) {
                solicitudExistente.setEstadoS(resupply.getEstadoS());
            }
            return solreabastecimientoRepository.save(resupply);
        } else {
            return null;
        }
    }

    


}
