package com.example.ms_operaciones.feign;

import com.example.ms_operaciones.dto.TransactionToWalletDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-wallet-service")
public interface WalletFeignClient {

    @PostMapping("/transactions/from-operaciones")
    void registrarDesdeOperacion(@RequestBody TransactionToWalletDTO dto);
}
