package com.example.ms_operaciones.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-contabilidad")
public interface ContabilidadClient {

    @PostMapping("/asientos/generar")
    void generarAsiento(@RequestBody Object request);
}
