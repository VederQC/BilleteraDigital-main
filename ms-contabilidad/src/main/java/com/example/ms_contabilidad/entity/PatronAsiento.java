package com.example.ms_contabilidad.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "patron_asiento")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PatronAsiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String patronCodigo;

    @Column(nullable = false, length = 150)
    private String patronNombre;

    @OneToMany(mappedBy = "patronAsiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatronAsientoDetalle> detalles;
}
