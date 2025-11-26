package com.example.ms_contabilidad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.ms_contabilidad.feign")
public class MsContabilidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsContabilidadApplication.class, args);
	}

}
