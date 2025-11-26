package com.example.ms_operaciones.dto;

import lombok.Data;

@Data
public class ClienteRequest {

    private String nombre;
    private String apellido;
    private String dni;
    private String ruc;
    private String numeroCel;
}
