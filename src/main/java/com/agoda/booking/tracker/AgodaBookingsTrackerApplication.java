package com.agoda.booking.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableRetry
public class AgodaBookingsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgodaBookingsTrackerApplication.class, args);
	}

}
