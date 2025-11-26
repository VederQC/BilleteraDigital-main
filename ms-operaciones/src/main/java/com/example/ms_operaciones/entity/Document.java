package com.example.ms_operaciones.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "document")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Enum: VENTA, COMPRA, COBRO, PAGO
    @Enumerated(EnumType.STRING)
    private OperationType tipo;

    private Long clienteId;
    private Long proveedorId;

    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;

    private LocalDateTime fecha;

    private String estado; // EMITIDO, CANCELADO

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentDetail> detalles;
}
