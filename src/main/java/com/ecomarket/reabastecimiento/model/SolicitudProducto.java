package com.ecomarket.reabastecimiento.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "solicitud_producto")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitudProd;

    @ManyToOne
    @JoinColumn(name = "id_solicitud")
    @JsonBackReference
    private Solreabastecimiento solicitudReabastecimiento;


    @Column(nullable = false)
    private Long idProducto;

    @Transient
    private Producto producto; 

    @Column(nullable = false)
    private int cantidad; 
}
