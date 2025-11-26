package com.example.ms_operaciones.repository;


import com.example.ms_operaciones.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByDni(String dni);

    boolean existsByRuc(String ruc);
}
