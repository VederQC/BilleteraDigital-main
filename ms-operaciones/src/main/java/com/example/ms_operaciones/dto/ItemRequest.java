package com.example.ms_operaciones.dto;



import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ItemRequest {
    private String producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
