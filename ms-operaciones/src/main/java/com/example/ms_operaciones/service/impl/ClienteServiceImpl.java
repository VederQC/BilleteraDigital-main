package com.example.ms_operaciones.service.impl;


import com.example.ms_operaciones.dto.ClienteRequest;
import com.example.ms_operaciones.dto.ClienteResponse;
import com.example.ms_operaciones.entity.Cliente;
import com.example.ms_operaciones.repository.ClienteRepository;
import com.example.ms_operaciones.service.ClienteService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse crearCliente(ClienteRequest request) {

        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .dni(request.getDni())
                .ruc(request.getRuc())
                .numeroCel(request.getNumeroCel())
                .build();

        Cliente saved = clienteRepository.save(cliente);

        return mapToResponse(saved);
    }

    @Override
    public ClienteResponse obtenerCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return mapToResponse(cliente);
    }

    @Override
    public List<ClienteResponse> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponse actualizarCliente(Long id, ClienteRequest request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setDni(request.getDni());
        cliente.setRuc(request.getRuc());
        cliente.setNumeroCel(request.getNumeroCel());

        Cliente updated = clienteRepository.save(cliente);

        return mapToResponse(updated);
    }

    @Override
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .ruc(cliente.getRuc())
                .numeroCel(cliente.getNumeroCel())
                .build();
    }
}
