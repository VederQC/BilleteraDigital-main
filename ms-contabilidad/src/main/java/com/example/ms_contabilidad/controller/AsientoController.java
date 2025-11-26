package com.example.ms_contabilidad.controller;



import com.example.ms_contabilidad.dto.NuevoAsientoRequest;
import com.example.ms_contabilidad.service.AsientoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asientos")
@RequiredArgsConstructor
public class AsientoController {

    private final AsientoService service;

    @PostMapping("/generar")
    public ResponseEntity<String> generar(@RequestBody NuevoAsientoRequest req) {
        service.generarAsiento(req);
        return ResponseEntity.ok("Asiento generado correctamente");
    }
}
