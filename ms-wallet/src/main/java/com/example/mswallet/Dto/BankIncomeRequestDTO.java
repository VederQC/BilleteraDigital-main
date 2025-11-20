package com.example.mswallet.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BankIncomeRequestDTO {
    private Long userId;
    private Long bankId;
    private BigDecimal amount;
    private String description;
}
