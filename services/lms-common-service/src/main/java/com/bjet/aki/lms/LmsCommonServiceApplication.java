package com.bjet.aki.lms;

import com.bjet.aki.lms.jpa.StudentEntity;
import com.bjet.aki.lms.jpa.UserEntity;
import com.bjet.aki.lms.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LmsCommonServiceApplication {
    public static void main(String[] args) {
		SpringApplication.run(LmsCommonServiceApplication.class, args);
	}
}
