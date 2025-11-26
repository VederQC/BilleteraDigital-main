package com.example.ms_contabilidad.repository;


import com.example.ms_contabilidad.entity.PatronAsiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatronAsientoRepository extends JpaRepository<PatronAsiento, Long> {
    Optional<PatronAsiento> findByPatronCodigo(String patronCodigo);
}
