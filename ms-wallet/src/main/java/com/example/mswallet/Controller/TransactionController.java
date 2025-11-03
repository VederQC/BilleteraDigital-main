package com.example.mswallet.Controller;



import com.example.mswallet.Dto.TransactionRequestDTO;
import com.example.mswallet.Dto.TransactionResponseDTO;
import com.example.mswallet.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO request) {
        TransactionResponseDTO response = transactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<TransactionResponseDTO> transactions =
                transactionService.getUserTransactions(userId, startDate, endDate);

        return ResponseEntity.ok(transactions);
    }
}
