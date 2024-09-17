package com.rhb.digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DigitalBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankApplication.class, args);
	}

}
