package com.example.ms_contabilidad.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "ms-pcge")
public interface PcgeClient {

    @GetMapping("/pcge/accounts/{code}")
    Map<String, Object> getAccountByCode(@PathVariable String code);
}
