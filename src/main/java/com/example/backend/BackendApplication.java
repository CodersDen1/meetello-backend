package com.example.backend;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class BackendApplication {

	@Value("${JWT_SECRET_KEY}")

	@Bean
	public ModelMapper modelMapper(){
		return  new ModelMapper();
	}


	public static void main(String[] args) {

		SpringApplication.run(BackendApplication.class, args);


	}

}
