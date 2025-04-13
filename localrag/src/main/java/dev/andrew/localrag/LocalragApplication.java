package dev.andrew.localrag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("dev.andrew.localrag.properties")
public class LocalragApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalragApplication.class, args);
	}

}
