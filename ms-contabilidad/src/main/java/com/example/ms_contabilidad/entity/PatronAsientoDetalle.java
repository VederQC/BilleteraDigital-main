package com.example.ms_contabilidad.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patron_asiento_detalle")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PatronAsientoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String etapa;      // PRINCIPAL, DESTINO, CANCELACION
    private Integer orden;
    private String cuentaCodigo;
    private String cuentaNombre;
    private String movimiento; // DEBE / HABER
    private String tipoMonto;  // BASE, IGV, TOTAL, COSTO, etc.
    private String glosa;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private PatronAsiento patronAsiento;
}
