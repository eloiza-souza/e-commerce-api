package com.zup.e_commerce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.Runtime.version;

@OpenAPIDefinition(
		info = @Info(
				title = "E-Commerce API",
				version = "1.3.0",
				description = "API para gerenciamento de produtos, clientes e compras em um sistema de e-commerce."
		)
)
@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
