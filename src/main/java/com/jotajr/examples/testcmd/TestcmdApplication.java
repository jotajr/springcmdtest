package com.jotajr.examples.testcmd;

import com.jotajr.examples.testcmd.services.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class TestcmdApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestcmdApplication.class);

	private final ExecutorService executorService;

	public TestcmdApplication(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public static void main(String[] args) {
		LOGGER.info("Starting the app");
		SpringApplication.run(TestcmdApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ArrayList<String> urlsToTest = new ArrayList<>();
		urlsToTest.add("google.com");
		urlsToTest.add("github.com");
		executorService.executeCmdApp(urlsToTest);
	}
}
