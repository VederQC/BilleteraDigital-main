package com.example.ms_contabilidad.repository;


import com.example.ms_contabilidad.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
}
