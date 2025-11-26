package com.example.ms_contabilidad.controller;


import com.example.ms_contabilidad.service.PatronAsientoExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/patrones")
@RequiredArgsConstructor
public class PatronAsientoController {

    private final PatronAsientoExcelService excelService;

    @PostMapping("/upload")
    public String subirPatrones(@RequestParam("file") MultipartFile file) throws Exception {
        return excelService.cargarExcel(file);
    }
}
