package com.ecomarket.reabastecimiento.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecomarket.reabastecimiento.model.Solreabastecimiento;
import com.ecomarket.reabastecimiento.service.SolreabastecimientoService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/hateoas")
public class SolreabastecimientoControllerV2 {

    @Autowired
    private SolreabastecimientoService solreabastecimientoService;

    @GetMapping(value = "/listar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Solreabastecimiento>>> listarSolicitudesConLinks() {
        List<Solreabastecimiento> solicitudes = solreabastecimientoService.getAll();

        if (solicitudes == null || solicitudes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<EntityModel<Solreabastecimiento>> solicitudModels = solicitudes.stream()
            .map(sol -> EntityModel.of(sol,
                linkTo(methodOn(SolreabastecimientoControllerV2.class).buscarSolicitud(sol.getIdSolicitud())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<Solreabastecimiento>> collectionModel = CollectionModel.of(
            solicitudModels,
            linkTo(methodOn(SolreabastecimientoControllerV2.class).listarSolicitudesConLinks()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<EntityModel<Solreabastecimiento>> buscarSolicitud(@PathVariable Long id) {
        Solreabastecimiento solicitud = solreabastecimientoService.findById(id);
        if (solicitud == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Solreabastecimiento> solicitudModel = EntityModel.of(
            solicitud,
            linkTo(methodOn(SolreabastecimientoControllerV2.class).buscarSolicitud(id)).withSelfRel(),
            linkTo(methodOn(SolreabastecimientoControllerV2.class).listarSolicitudesConLinks()).withRel("solicitudes")
        );

        return ResponseEntity.ok(solicitudModel);
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Solreabastecimiento>>> buscarSolicitudesV2() {
        List<Solreabastecimiento> solreabastecimiento = solreabastecimientoService.getAll();
        if (solreabastecimiento == null || solreabastecimiento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<EntityModel<Solreabastecimiento>> solreabastecimientoModels = solreabastecimiento.stream()
                .map(sol -> EntityModel.of(sol,
                        linkTo(methodOn(SolreabastecimientoControllerV2.class)
                        .buscarSolicitud(sol.getIdSolicitud())).withSelfRel()))
                .toList();

        CollectionModel<EntityModel<Solreabastecimiento>> collectionModel = CollectionModel.of(
            solreabastecimientoModels,
            linkTo(methodOn(SolreabastecimientoControllerV2.class).buscarSolicitudesV2()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

}
