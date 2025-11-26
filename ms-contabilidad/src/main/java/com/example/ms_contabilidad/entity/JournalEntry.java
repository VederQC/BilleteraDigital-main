package com.example.ms_contabilidad.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "journal_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentoId;

    @Enumerated(EnumType.STRING)
    private OperationType tipoOperacion;

    private LocalDateTime fecha;

    private String descripcion;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryLine> lineas;  // 👈 aquí

}
