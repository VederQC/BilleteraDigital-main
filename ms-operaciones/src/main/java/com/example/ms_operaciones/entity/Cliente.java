package com.example.ms_operaciones.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String dni;        // 8 dígitos

    private String ruc;        // 11 dígitos

    private String numeroCel;  // número telefónico
}
