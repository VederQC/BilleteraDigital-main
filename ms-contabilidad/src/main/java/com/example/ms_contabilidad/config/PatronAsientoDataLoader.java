package com.example.ms_contabilidad.config;


import com.example.ms_contabilidad.entity.PatronAsiento;
import com.example.ms_contabilidad.entity.PatronAsientoDetalle;
import com.example.ms_contabilidad.repository.PatronAsientoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PatronAsientoDataLoader implements CommandLineRunner {

    private final PatronAsientoRepository patronRepo;

    @Override
    public void run(String... args) throws Exception {

        long count = patronRepo.count();
        System.out.println("Hibernate count patrones: " + count);

        // ✔ Evita recargar si ya existe
        if (count > 0) {
            System.out.println("✔ Patrones ya cargados, no se vuelven a insertar.");
            return;
        }

        System.out.println("→ Cargando PATRONES DE ASIENTOS desde Excel...");

        InputStream file = new ClassPathResource("contabilidad/patrones_asientos.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, PatronAsiento> mapPatrones = new LinkedHashMap<>();

        DataFormatter fmt = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            String patronCodigo = fmt.formatCellValue(row.getCell(0)).trim();
            String patronNombre = fmt.formatCellValue(row.getCell(1)).trim();
            String etapa = fmt.formatCellValue(row.getCell(2)).trim();
            String ordenStr = fmt.formatCellValue(row.getCell(3)).trim();
            String cuentaCodigo = fmt.formatCellValue(row.getCell(4)).trim();
            String cuentaNombre = fmt.formatCellValue(row.getCell(5)).trim();
            String movimiento = fmt.formatCellValue(row.getCell(6)).trim();
            String tipoMonto = fmt.formatCellValue(row.getCell(7)).trim();
            String glosa = fmt.formatCellValue(row.getCell(8)).trim();

            // ✔ Validación básica
            if (patronCodigo.isEmpty() || patronNombre.isEmpty() || cuentaCodigo.isEmpty())
                continue;

            // ✔ Crear patron si no existe
            PatronAsiento patron = mapPatrones.computeIfAbsent(
                    patronCodigo,
                    k -> PatronAsiento.builder()
                            .patronCodigo(patronCodigo)
                            .patronNombre(patronNombre)
                            .detalles(new ArrayList<>())
                            .build()
            );

            PatronAsientoDetalle det = PatronAsientoDetalle.builder()
                    .etapa(etapa)
                    .orden(ordenStr.isEmpty() ? null : Integer.parseInt(ordenStr))
                    .cuentaCodigo(cuentaCodigo)
                    .cuentaNombre(cuentaNombre)
                    .movimiento(movimiento)
                    .tipoMonto(tipoMonto)
                    .glosa(glosa)
                    .patronAsiento(patron)
                    .build();

            patron.getDetalles().add(det);
        }

        // ✔ Persistir todos los patrones con sus detalles
        patronRepo.saveAll(mapPatrones.values());

        System.out.println("✔ Patrones cargados correctamente: " + mapPatrones.size());
    }
}
