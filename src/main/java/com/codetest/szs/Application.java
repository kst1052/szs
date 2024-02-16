package com.codetest.szs;

import com.codetest.szs.encrypt.BcryptImpl;
import com.codetest.szs.encrypt.EncryptHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

/*	@Bean
	public EncryptHelper encryptConfig() {
		return new BcryptImpl();
	}*/
}
