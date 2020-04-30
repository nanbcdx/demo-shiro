package com.wph.demoshiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wph.demoshiro.mapper")
public class DemoShiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoShiroApplication.class, args);
	}

}
