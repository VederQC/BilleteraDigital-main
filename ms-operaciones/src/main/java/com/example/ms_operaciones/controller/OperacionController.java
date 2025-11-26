package com.example.ms_operaciones.controller;

import com.example.ms_operaciones.dto.OperacionSimpleRequest;
import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;
import com.example.ms_operaciones.service.OperacionService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/operaciones")
@RequiredArgsConstructor
public class OperacionController {

    private final OperacionService operacionService;

    // ========================== CREAR ==========================

    @PostMapping("/ventas")
    public ResponseEntity<Document> registrarVenta(@RequestBody VentaRequest req) {
        return ResponseEntity.ok(operacionService.registrarVenta(req));
    }

    @PostMapping("/compras")
    public ResponseEntity<Document> registrarCompra(@RequestBody VentaRequest req) {
        return ResponseEntity.ok(operacionService.registrarCompra(req));
    }

    @PostMapping("/cobros")
    public ResponseEntity<Document> registrarCobro(@RequestBody OperacionSimpleRequest req) {
        return ResponseEntity.ok(operacionService.registrarCobro(req));
    }

    @PostMapping("/pagos")
    public ResponseEntity<Document> registrarPago(@RequestBody OperacionSimpleRequest req) {
        return ResponseEntity.ok(operacionService.registrarPago(req));
    }

    // ========================== LISTAR ==========================

    @GetMapping
    public ResponseEntity<List<Document>> listarTodo() {
        return ResponseEntity.ok(operacionService.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(operacionService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Document>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(operacionService.listarPorCliente(clienteId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Document>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(operacionService.listarPorTipo(tipo));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<Document>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(operacionService.listarPorFechas(start, end));
    }

    @GetMapping("/cliente/{clienteId}/fechas")
    public ResponseEntity<List<Document>> listarPorClienteYFechas(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(operacionService.listarPorClienteYFechas(clienteId, start, end));
    }
}