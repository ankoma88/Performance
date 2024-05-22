package com.ankoma88.datastructures.performance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.logging.Logger;

@SpringBootApplication
public class PerformanceApplication {
	private static final Logger LOGGER = Logger.getLogger( PerformanceApplication.class.getName());

	@KafkaListener(topics = "topic1", groupId = "group1")
	void listener(String data) {
		LOGGER.info(data);
	}

	public static void main(String[] args) {
		SpringApplication.run(PerformanceApplication.class, args);
	}
}
