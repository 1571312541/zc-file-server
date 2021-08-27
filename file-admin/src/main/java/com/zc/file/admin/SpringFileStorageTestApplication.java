package com.zc.file.admin;

import com.zc.file.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFileStorage
@MapperScan("com.zc.file.admin.mapper")
public class SpringFileStorageTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFileStorageTestApplication.class, args);
	}

}
