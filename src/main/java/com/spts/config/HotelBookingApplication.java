package com.spts.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The packages of rest controllers must be the sub packages of  package of this class or use "scanbasepackages property as below"
@SpringBootApplication(scanBasePackages = {"com.spts.booking", "com.spts.login","com.spts.search","com.spts.signup","com.spts.admin","com.spts.helper","com.spts.misc"})
public class HotelBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApplication.class, args);
	}

}
