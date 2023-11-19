package com.barack.securebanksystem;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info =  @Info(
				title = "Secured Banking System App",
				description = "Backend REST API for a Bank",
				version = "1.0",
				contact = @Contact(
						name = "Hussein Barack",
						email = "barack@gmail.com",
						url= "https://github.com/oussenimbohou"
				),
				license = @License(
						name = "Hussein Barack",
						url = "https://github.com/oussenimbohou"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Secured Banking System Documentation",
				url = "https://github.com/oussenimbohou"
		)
)
public class SecureBankSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecureBankSystemApplication.class, args);
	}

}
