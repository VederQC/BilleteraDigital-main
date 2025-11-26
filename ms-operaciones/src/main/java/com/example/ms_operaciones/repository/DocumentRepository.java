package com.example.ms_operaciones.repository;



import com.example.ms_operaciones.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
