package com.medical_drones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicalDronesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalDronesApplication.class, args);
	}
}
