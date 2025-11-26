package com.example.ms_operaciones.service;

import com.example.ms_operaciones.dto.OperacionSimpleRequest;
import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;

import java.time.LocalDate;
import java.util.List;

public interface OperacionService {

    Document registrarVenta(VentaRequest request);

    Document registrarCompra(VentaRequest request);

    Document registrarCobro(OperacionSimpleRequest request);

    Document registrarPago(OperacionSimpleRequest request);

    // ================= LISTADOS =================

    List<Document> listarTodo();

    Document obtenerPorId(Long id);

    List<Document> listarPorCliente(Long clienteId);

    List<Document> listarPorTipo(String tipo);

    List<Document> listarPorFechas(LocalDate start, LocalDate end);

    List<Document> listarPorClienteYFechas(Long clienteId, LocalDate start, LocalDate end);
}
