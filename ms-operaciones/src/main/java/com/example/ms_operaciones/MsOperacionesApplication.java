package com.example.ms_operaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.ms_operaciones.feign")
public class MsOperacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOperacionesApplication.class, args);
	}

}
