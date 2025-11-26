package com.example.ms_pcge.service.impl;


import com.example.ms_pcge.entity.PcgeAccount;
import com.example.ms_pcge.repository.PcgeAccountRepository;
import com.example.ms_pcge.service.PcgeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PcgeAccountServiceImpl implements PcgeAccountService {

    private final PcgeAccountRepository repository;

    @Override
    public PcgeAccount save(PcgeAccount account) {
        return repository.save(account);
    }

    @Override
    public void saveAll(List<PcgeAccount> accounts) {
        repository.saveAll(accounts);
    }

    @Override
    public List<PcgeAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public PcgeAccount findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Account not found: " + code));
    }

    @Override
    public List<PcgeAccount> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}
