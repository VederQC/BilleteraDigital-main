package com.example.ms_contabilidad.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entry_template_line")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntryTemplateLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountCode;   // ejemplo: 12
    private String side;          // "DEBE" o "HABER"
    private String formula;       // SUBTOTAL, IGV, TOTAL

    @ManyToOne
    @JoinColumn(name = "template_id")
    private EntryTemplate template;
}
