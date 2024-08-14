package com.bookingmeetingroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bookingmeetingroom")
public class BookingMeetingRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingMeetingRoomApplication.class, args);
	}
}
