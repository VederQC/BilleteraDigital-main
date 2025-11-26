package com.example.ms_operaciones.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OperacionSimpleRequest {
    private Long clienteId;
    private BigDecimal monto;
}
