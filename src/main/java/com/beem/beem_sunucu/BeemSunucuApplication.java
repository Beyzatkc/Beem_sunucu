package com.beem.beem_sunucu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeemSunucuApplication {

	public static void main(String[] args) {
		System.out.println("Servis çalıştı");
		SpringApplication.run(BeemSunucuApplication.class, args);
	}

}
