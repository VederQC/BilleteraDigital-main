package com.example.ms_contabilidad.repository;



import com.example.ms_contabilidad.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}
