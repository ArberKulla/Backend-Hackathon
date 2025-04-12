package com.elinikon.merrbio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.elinikon.merrbio")
public class MerrBioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerrBioApplication.class, args);
	}

}
