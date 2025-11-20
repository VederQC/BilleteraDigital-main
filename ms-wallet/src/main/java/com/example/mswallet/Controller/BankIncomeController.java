package com.example.mswallet.Controller;

import com.example.mswallet.Dto.BankIncomeRequestDTO;
import com.example.mswallet.Dto.TransactionResponseDTO;
import com.example.mswallet.Service.BankIncomeService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banks")
@RequiredArgsConstructor
public class BankIncomeController {

    private final BankIncomeService bankIncomeService;

    @PostMapping("/add-income")
    public ResponseEntity<TransactionResponseDTO> addIncomeToWallet(
            @RequestBody BankIncomeRequestDTO dto) {

        return ResponseEntity.ok(bankIncomeService.addIncomeToWallet(dto));
    }
}
