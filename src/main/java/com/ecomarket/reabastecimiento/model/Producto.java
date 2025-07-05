package com.ecomarket.reabastecimiento.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private Long idProducto;
    private String nombreProducto;
    private String codigoProducto;
}
