package com.example.ms_operaciones.entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "document_detail")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DocumentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal totalItem;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;
}
