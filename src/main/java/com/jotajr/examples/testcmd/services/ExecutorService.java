package com.jotajr.examples.testcmd.services;

import com.jotajr.examples.testcmd.beans.TestResultBean;
import com.jotajr.examples.testcmd.util.SpringCmdTestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorService.class);


    public void executeCmdApp(List<String> urlListToPing) {

        if(urlListToPing.isEmpty()) {
            LOGGER.info("List of urls is empty. Try again!");
            return;
        }

        LOGGER.info("Starting Execution");
        createResultDirectory();

        String systemOs = System.getProperty("os.name")
                .toLowerCase();

        ArrayList<TestResultBean> listResultBean = new ArrayList<>();

        LOGGER.debug("System name: {}", systemOs);

        if (systemOs.startsWith("windows")) {

            for (String url :
                    urlListToPing) {
                TestResultBean testeResult = processPingJob(url);
                if(testeResult != null) {
                    listResultBean.add(testeResult);
                }
            }

            if (!listResultBean.isEmpty()) {
                writeResults(listResultBean);
            }

        } else {
            LOGGER.error("Os not supported yet!");
        }

    }

    private TestResultBean processPingJob(String pingUrl) {

        TestResultBean testResultBean = new TestResultBean();
        StringBuilder resultString = new StringBuilder();

        LOGGER.debug("Starting a ping command to url: {}", pingUrl);

        String fullCommand = "ping " + pingUrl;

        LOGGER.debug("Executing command: {}", fullCommand);

        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", fullCommand);

        try {

            long start = new Date().getTime();
            testResultBean.setStartTime(SpringCmdTestUtil.getActualTimeStamp());

            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultString.append(line);
            }

            int procResult = process.waitFor();
            if(procResult != 0) {
                LOGGER.error("There is an error on process. Returning result null.");
                return null;
            }

            LOGGER.debug("Process ended with result code {}", procResult);

            long finish = new Date().getTime();
            testResultBean.setResult(resultString.toString());
            testResultBean.setFinishTime(SpringCmdTestUtil.getActualTimeStamp());
            testResultBean.setDuration(Long.toString(finish - start));
            LOGGER.debug("Process duration: {} milliseconds.", finish - start);


        } catch (IOException e) {
            LOGGER.error("Fail to execute the process. Error: {}", e.getMessage());
            return null;
        } catch (InterruptedException e) {
            LOGGER.error("There is an interruption in process. Error: {}", e.getMessage());
            return null;
        }

        return testResultBean;

    }

    private void createResultDirectory() {

        LOGGER.debug("Creating results directory if not exists");

        Path path = Paths.get("results");
        if (!Files.exists(path)) {
            LOGGER.debug("The results directory not exists, creating one");
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                LOGGER.error("Failed to create results directory. Error: {}", e.getMessage());
                System.exit(-1);
            }
        } else {
            LOGGER.debug("Directory results already exists!");
        }

    }

    private void writeResults(List<TestResultBean> resultList) {

        Charset utf8 = StandardCharsets.UTF_8;
        String pathString = "results/" + SpringCmdTestUtil.getFilename();
        Path pathWrite = Paths.get(pathString);

        ArrayList<String> lines = new ArrayList<>();
        lines.add("START_TIME|RESULT|FINISH_TIME|DURATION");

        for (TestResultBean testResult :
                resultList) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(testResult.getStartTime());
            stringBuilder.append("|");
            stringBuilder.append(testResult.getResult());
            stringBuilder.append("|");
            stringBuilder.append(testResult.getFinishTime());
            stringBuilder.append("|");
            stringBuilder.append(testResult.getDuration());
            lines.add(stringBuilder.toString());
        }

        try {

            Files.write(pathWrite, lines, utf8);

        } catch (IOException e) {
            LOGGER.error("Fail to write results file. Error: {}", e.getMessage());
        }

    }

}
