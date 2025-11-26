package com.example.ms_contabilidad.service.impl;

import com.example.ms_contabilidad.dto.NuevoAsientoRequest;
import com.example.ms_contabilidad.entity.*;
import com.example.ms_contabilidad.feign.PcgeClient;
import com.example.ms_contabilidad.repository.JournalEntryRepository;
import com.example.ms_contabilidad.service.AsientoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsientoServiceImpl implements AsientoService {

    private final JournalEntryRepository entryRepo;
    private final PcgeClient pcgeClient;

    @Override
    public void generarAsiento(NuevoAsientoRequest req) {

        // Convertimos el String del request a ENUM interno
        OperationType tipo = OperationType.valueOf(req.getTipoOperacion());

        // 1. Crear CABECERA del asiento
        JournalEntry entry = JournalEntry.builder()
                .documentoId(req.getDocumentoId())
                .tipoOperacion(tipo)
                .fecha(req.getFecha())
                .descripcion("Asiento generado automáticamente")
                .build();

        List<JournalEntryLine> lines = new ArrayList<>();

        // 2. Crear líneas según el tipo de operación
        switch (tipo) {
            case VENTA -> generarVenta(req, entry, lines);
            case COMPRA -> generarCompra(req, entry, lines);
            case COBRO -> generarCobro(req, entry, lines);
            case PAGO -> generarPago(req, entry, lines);
        }

        // 3. Guardar líneas dentro del asiento
        entry.setLineas(lines);

        // 4. Guardar en BD
        entryRepo.save(entry);
    }


    // =============================
    //   ASIENTOS CONTABLES
    // =============================

    private void generarVenta(NuevoAsientoRequest req, JournalEntry entry, List<JournalEntryLine> lines) {
        // 1212 → Cuentas por cobrar
        lines.add(crearLinea("1212", req.getTotal(), BigDecimal.ZERO, entry));

        // 7011 → Venta
        lines.add(crearLinea("7011", BigDecimal.ZERO, req.getSubtotal(), entry));

        // 4011 → IGV por pagar
        lines.add(crearLinea("4011", BigDecimal.ZERO, req.getIgv(), entry));
    }

    private void generarCompra(NuevoAsientoRequest req, JournalEntry entry, List<JournalEntryLine> lines) {
        // 6011 → Compras
        lines.add(crearLinea("6011", req.getSubtotal(), BigDecimal.ZERO, entry));

        // 4011 → IGV por pagar
        lines.add(crearLinea("4011", BigDecimal.ZERO, req.getIgv(), entry));

        // 4212 → Cuentas por pagar
        lines.add(crearLinea("4212", BigDecimal.ZERO, req.getTotal(), entry));
    }

    private void generarCobro(NuevoAsientoRequest req, JournalEntry entry, List<JournalEntryLine> lines) {
        // 1011 → Caja
        lines.add(crearLinea("1011", req.getTotal(), BigDecimal.ZERO, entry));

        // 1212 → Cuentas por cobrar
        lines.add(crearLinea("1212", BigDecimal.ZERO, req.getTotal(), entry));
    }

    private void generarPago(NuevoAsientoRequest req, JournalEntry entry, List<JournalEntryLine> lines) {
        // 4212 → Cuentas por pagar
        lines.add(crearLinea("4212", req.getTotal(), BigDecimal.ZERO, entry));

        // 1011 → Caja
        lines.add(crearLinea("1011", BigDecimal.ZERO, req.getTotal(), entry));
    }


    // =============================
    //   MÉTODO GENERAL PARA LÍNEAS
    // =============================

    private JournalEntryLine crearLinea(String cuenta, BigDecimal debe, BigDecimal haber, JournalEntry entry) {

        var data = pcgeClient.getAccountByCode(cuenta);
        String nombreCuenta = (String) data.get("name");

        return JournalEntryLine.builder()
                .cuenta(cuenta)
                .nombreCuenta(nombreCuenta)
                .debe(debe)
                .haber(haber)
                .entry(entry)
                .build();
    }

}
