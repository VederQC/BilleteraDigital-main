package com.example.ms_operaciones.repository;

import com.example.ms_operaciones.entity.Document;
import com.example.ms_operaciones.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByClienteId(Long clienteId);

    List<Document> findByTipo(OperationType tipo);

    List<Document> findByFechaBetween(LocalDateTime start, LocalDateTime end);

    List<Document> findByClienteIdAndFechaBetween(Long clienteId, LocalDateTime start, LocalDateTime end);
}
