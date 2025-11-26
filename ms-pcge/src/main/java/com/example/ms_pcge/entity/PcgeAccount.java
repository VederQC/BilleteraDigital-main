package com.example.ms_pcge.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pcge_account")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PcgeAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código de la cuenta (101, 40111, 70, etc.)
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    // Nombre de la cuenta (Caja, IGV por pagar, Ventas, etc.)
    @Column(nullable = false)
    private String name;

    // Clase PCGE (1–9: Activo, Pasivo, Patrimonio, etc.)
    private Integer classNumber;

    // Nivel dentro de la jerarquía (1: grupo, 2: subgrupo, etc.)
    private Integer level;

    // Código de cuenta padre (ej: 40 es padre de 401, 401 es padre de 40111)
    private String parentCode;

    // Naturaleza (D = Deudora, H = Acreedora)
    private String nature;

    // Tipo general (ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO, COSTO)
    private String type;
}
