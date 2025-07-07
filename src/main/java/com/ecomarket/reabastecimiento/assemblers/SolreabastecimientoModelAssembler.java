package com.ecomarket.reabastecimiento.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.ecomarket.reabastecimiento.model.Solreabastecimiento;
import com.ecomarket.reabastecimiento.controller.SolreabastecimientoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class SolreabastecimientoModelAssembler extends RepresentationModelAssemblerSupport<Solreabastecimiento, EntityModel<Solreabastecimiento>>{
    
    public SolreabastecimientoModelAssembler() {
        super(SolreabastecimientoController.class, (Class<EntityModel<Solreabastecimiento>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<Solreabastecimiento> toModel(Solreabastecimiento solreabastecimiento) {
        return EntityModel.of(solreabastecimiento,
                linkTo(methodOn(SolreabastecimientoController.class).getById(solreabastecimiento.getIdSolicitud(), solreabastecimiento)).withSelfRel(),
                linkTo(methodOn(SolreabastecimientoController.class).getAll()).withRel("Solicitudes"));
    }
}
