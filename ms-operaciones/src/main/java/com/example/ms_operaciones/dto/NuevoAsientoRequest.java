package com.example.ms_operaciones.dto;



import com.example.ms_operaciones.entity.Document;

import com.example.ms_operaciones.entity.OperationType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NuevoAsientoRequest {

    private Long documentoId;
    private String tipoOperacion;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private LocalDateTime fecha;
    private Long clienteId;
    private Long proveedorId;

    public NuevoAsientoRequest(Document document) {
        this.documentoId = document.getId();
        this.tipoOperacion = document.getTipo().name();
        this.subtotal = document.getSubtotal();
        this.igv = document.getIgv();
        this.total = document.getTotal();
        this.fecha = document.getFecha();
        this.clienteId = document.getClienteId();
        this.proveedorId = document.getProveedorId();
    }
}
