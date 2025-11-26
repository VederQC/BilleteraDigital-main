package com.example.ms_pcge.config;


import com.example.ms_pcge.entity.PcgeAccount;
import com.example.ms_pcge.repository.PcgeAccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class PcgeDataLoader implements CommandLineRunner {

    private final PcgeAccountRepository repository;

    @Override
    public void run(String... args) throws Exception {

        long count = repository.count();
        System.out.println("Hibernate count: " + count);

        // ✔ Si YA EXISTE DATA → NO cargamos nada
        if (count > 0) {
            System.out.println("✔ PCGE ya cargado, no se vuelve a insertar.");
            return;
        }

        System.out.println("→ Cargando PCGE 2019 desde Excel...");

        InputStream file = new ClassPathResource("pcge/pcge_2019.xlsx").getInputStream();
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<PcgeAccount> accounts = new ArrayList<>();
        Set<String> uniqueCodes = new HashSet<>(); // ✔ evita duplicados

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;
            if (i == 0) continue; // saltar encabezado

            DataFormatter fmt = new DataFormatter();

            String code = fmt.formatCellValue(row.getCell(0)).trim();
            String name = fmt.formatCellValue(row.getCell(1)).trim();
            String classNumStr = fmt.formatCellValue(row.getCell(2)).trim();
            String levelStr = fmt.formatCellValue(row.getCell(3)).trim();
            String parentCode = fmt.formatCellValue(row.getCell(4)).trim();
            String nature = fmt.formatCellValue(row.getCell(5)).trim();
            String type = fmt.formatCellValue(row.getCell(6)).trim();

            if (code.isEmpty() || name.isEmpty()) continue;

            // ✔ Si está repetido lo saltamos
            if (uniqueCodes.contains(code)) {
                System.out.println("⚠ Código duplicado ignorado: " + code);
                continue;
            }

            uniqueCodes.add(code);

            PcgeAccount pc = PcgeAccount.builder()
                    .code(code)
                    .name(name)
                    .classNumber(classNumStr.isEmpty() ? null : Integer.parseInt(classNumStr))
                    .level(levelStr.isEmpty() ? null : Integer.parseInt(levelStr))
                    .parentCode(parentCode.isEmpty() ? null : parentCode)
                    .nature(nature)
                    .type(type)
                    .build();

            accounts.add(pc);
        }

        repository.saveAll(accounts);
        System.out.println("✔ PCGE cargado correctamente (" + accounts.size() + " cuentas)");
    }
}
