package com.example.ms_operaciones.dto;



import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class VentaRequest {

    private Long clienteId;

    private List<ItemRequest> items;

    // opcional si deseas enviar subtotal directo, pero vamos a calcularlo
}
