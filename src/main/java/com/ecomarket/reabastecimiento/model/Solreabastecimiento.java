package com.ecomarket.reabastecimiento.model;

import java.util.List;

import com.ecomarket.reabastecimiento.enums.EstadoSolicitud;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud_resupply")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Solreabastecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud", unique = true)
    private Long idSolicitud;

    @Column(nullable = false)
    private String direccionEntrega;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoS;
    
    @Column(nullable = false)
    private Long idEncargado;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 50, nullable = false)
    private String nombre;
    
    @Column(length = 50, nullable = false)
    private Long idProveedor;

    @Column(length = 50, nullable = false)
    private String nombreProveedor;

    @OneToMany(mappedBy = "solicitudReabastecimiento", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SolicitudProducto> productosSolicitados;
}
