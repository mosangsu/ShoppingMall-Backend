package com.uniwear.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
class BackendApplicationTests {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
		System.out.println("안녕하세요");
		System.out.println(passwordEncoder.encode("tkddnjs22@"));
	}
}
