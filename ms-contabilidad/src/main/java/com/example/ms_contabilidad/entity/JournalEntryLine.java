package com.example.ms_contabilidad.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "journal_entry_line")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class JournalEntryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cuenta;          // ← ESTO ES LO QUE TE FALTABA
    private String nombreCuenta;    // nombre obtenido del PCGE
    private BigDecimal debe;
    private BigDecimal haber;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private JournalEntry entry;
}
