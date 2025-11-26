package com.example.ms_pcge.service;


import com.example.ms_pcge.entity.PcgeAccount;


import java.util.List;

public interface PcgeAccountService {

    PcgeAccount save(PcgeAccount account);

    List<PcgeAccount> findAll();

    PcgeAccount findByCode(String code);

    List<PcgeAccount> searchByName(String name);

    void saveAll(List<PcgeAccount> accounts);
}
