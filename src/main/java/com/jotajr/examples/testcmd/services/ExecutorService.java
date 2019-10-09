package com.jotajr.examples.testcmd.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorService.class);


    public void executeCmdApp() {

        LOGGER.info("Starting Execution");

        String systemOs = System.getProperty("os.name")
                .toLowerCase();

        LOGGER.debug("System name: {}", systemOs);

        if(systemOs.startsWith("windows")) {
            LOGGER.info(processPingJob("google.com"));
            LOGGER.info(processPingJob("facebook.com"));
            LOGGER.info(processPingJob("pinterest.com"));
        } else {
            LOGGER.error("Os not supported yet!");
        }

    }

    private String processPingJob(String pingUrl) {

        StringBuilder commandResult = new StringBuilder();

        LOGGER.debug("Starting a ping command to url: {}", pingUrl);

        String fullCommand = "ping " + pingUrl;

        LOGGER.debug("Executing command: {}", fullCommand);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", fullCommand);

        try {

            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                commandResult.append(line);
            }

            int procResult = process.waitFor();
            LOGGER.debug("Process ended with result code {}", procResult);

        } catch (IOException e) {
            LOGGER.error("Fail to execute the process. Error: {}", e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("There is an interruption in process. Error: {}", e.getMessage());
        }

        return commandResult.toString();

    }

}
