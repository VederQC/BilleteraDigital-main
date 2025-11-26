package com.example.ms_operaciones.controller;



import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;
import com.example.ms_operaciones.service.OperacionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operaciones")
@RequiredArgsConstructor
public class OperacionController {

    private final OperacionService operacionService;

    @PostMapping("/ventas")
    public ResponseEntity<Document> registrarVenta(@RequestBody VentaRequest request) {
        return ResponseEntity.ok(operacionService.registrarVenta(request));
    }
}
