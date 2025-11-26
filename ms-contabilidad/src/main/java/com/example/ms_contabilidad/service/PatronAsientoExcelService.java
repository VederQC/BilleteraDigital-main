package com.example.ms_contabilidad.service;


import com.example.ms_contabilidad.entity.PatronAsiento;
import com.example.ms_contabilidad.entity.PatronAsientoDetalle;
import com.example.ms_contabilidad.repository.PatronAsientoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PatronAsientoExcelService {

    private final PatronAsientoRepository patronRepo;

    public String cargarExcel(MultipartFile file) throws Exception {

        InputStream is = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, PatronAsiento> mapaPatrones = new HashMap<>();

        for (Row row : sheet) {

            if (row.getRowNum() == 0) continue; // Cabecera

            String patronCodigo = row.getCell(0).getStringCellValue();
            String patronNombre = row.getCell(1).getStringCellValue();
            String etapa       = row.getCell(2).getStringCellValue();
            Integer orden      = (int) row.getCell(3).getNumericCellValue();
            String cuentaCodigo= row.getCell(4).getStringCellValue();
            String cuentaNombre= row.getCell(5).getStringCellValue();
            String movimiento  = row.getCell(6).getStringCellValue();
            String tipoMonto   = row.getCell(7).getStringCellValue();
            String glosa       = row.getCell(8).getStringCellValue();

            // si no existe el patrón, lo creamos
            PatronAsiento patron = mapaPatrones.computeIfAbsent(
                    patronCodigo,
                    code -> PatronAsiento.builder()
                            .patronCodigo(code)
                            .patronNombre(patronNombre)
                            .detalles(new ArrayList<>())
                            .build()
            );

            // detalle
            PatronAsientoDetalle det = PatronAsientoDetalle.builder()
                    .etapa(etapa)
                    .orden(orden)
                    .cuentaCodigo(cuentaCodigo)
                    .cuentaNombre(cuentaNombre)
                    .movimiento(movimiento)
                    .tipoMonto(tipoMonto)
                    .glosa(glosa)
                    .patronAsiento(patron)
                    .build();

            patron.getDetalles().add(det);
        }

        // persistir todos los patrones
        patronRepo.saveAll(mapaPatrones.values());

        return "Patrones cargados correctamente: " + mapaPatrones.size();
    }
}
