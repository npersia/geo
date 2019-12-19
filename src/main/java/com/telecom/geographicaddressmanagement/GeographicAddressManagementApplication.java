package com.telecom.geographicaddressmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// new - start
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
// new - end

@SpringBootApplication
@EnableCaching
public class GeographicAddressManagementApplication {

	public static void main(String[] args) {
		// SpringApplication.run(GeographicAddressManagementApplication.class, args);
		new SpringApplicationBuilder().sources(GeographicAddressManagementApplication.class).run(args);
	}

}		
