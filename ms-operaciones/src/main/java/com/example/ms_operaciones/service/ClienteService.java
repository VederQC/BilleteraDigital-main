package com.example.ms_operaciones.service;


import com.example.ms_operaciones.dto.ClienteRequest;
import com.example.ms_operaciones.dto.ClienteResponse;

import java.util.List;

public interface ClienteService {

    ClienteResponse crearCliente(ClienteRequest request);

    ClienteResponse obtenerCliente(Long id);

    List<ClienteResponse> listarClientes();

    ClienteResponse actualizarCliente(Long id, ClienteRequest request);

    void eliminarCliente(Long id);
}
