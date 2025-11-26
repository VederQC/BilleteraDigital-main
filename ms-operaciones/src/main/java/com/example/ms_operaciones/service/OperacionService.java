package com.example.ms_operaciones.service;



import com.example.ms_operaciones.dto.VentaRequest;
import com.example.ms_operaciones.entity.Document;

public interface OperacionService {


    Document registrarVenta(VentaRequest request);

}
