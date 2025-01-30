package com.example.codewavebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;  // 추가

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.member", "com.example.codewavebe"})  // 전체 패키지 스캔
public class CodeWaveBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(CodeWaveBeApplication.class, args);
	}
}
