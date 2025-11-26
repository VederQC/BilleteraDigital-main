package com.example.ms_pcge.controller;


import com.example.ms_pcge.entity.PcgeAccount;
import com.example.ms_pcge.service.PcgeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pcge")
@RequiredArgsConstructor
public class PcgeAccountController {

    private final PcgeAccountService service;

    @GetMapping("/accounts")
    public ResponseEntity<List<PcgeAccount>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/accounts/{code}")
    public ResponseEntity<PcgeAccount> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @GetMapping("/accounts/search")
    public ResponseEntity<List<PcgeAccount>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }
}
