package com.example.mswallet.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BankIncomeResponseDTO {
    private Long id;
    private Long userId;
    private Long bankId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
}
