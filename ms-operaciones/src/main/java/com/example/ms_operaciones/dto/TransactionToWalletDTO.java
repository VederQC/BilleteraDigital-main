package com.example.ms_operaciones.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionToWalletDTO {

    private Long userId;
    private BigDecimal amount;
    private String description;
    private String type; // INCOME o EXPENSE
}
