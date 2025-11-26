package com.example.ms_contabilidad.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PcgeAccountResponse {
    private String code;
    private String name;
    private String nature;  // D o H
    private String type;
}
