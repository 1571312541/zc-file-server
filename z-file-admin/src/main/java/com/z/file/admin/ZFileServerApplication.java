package com.z.file.admin;

import com.z.file.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFileStorage
@MapperScan("com.z.file.admin.mapper")
public class ZFileServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZFileServerApplication.class, args);
	}

}
