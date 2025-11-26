package com.example.ms_contabilidad.dto;



import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NuevoAsientoRequest {

    private Long documentoId;
    private String tipoOperacion;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private LocalDateTime fecha;
    private Long clienteId;
    private Long proveedorId;
}
