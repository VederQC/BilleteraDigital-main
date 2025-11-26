package com.example.ms_contabilidad.entity;



import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "entry_template")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntryTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // VENTA, COMPRA, COBRO, PAGO
    @Enumerated(EnumType.STRING)
    private OperationType tipoOperacion;

    private String nombre; // "Asiento de destino de venta"

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryTemplateLine> lineas;
}
