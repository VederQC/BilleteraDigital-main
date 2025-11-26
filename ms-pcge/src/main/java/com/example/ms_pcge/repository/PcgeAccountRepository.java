package com.example.ms_pcge.repository;


import com.example.ms_pcge.entity.PcgeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PcgeAccountRepository extends JpaRepository<PcgeAccount, Long> {

    Optional<PcgeAccount> findByCode(String code);

    List<PcgeAccount> findByNameContainingIgnoreCase(String name);
}
