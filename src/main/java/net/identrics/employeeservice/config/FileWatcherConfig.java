package net.identrics.employeeservice.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.identrics.employeeservice.service.CsvFileAddListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.time.Duration;

@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(EmployeeServiceProperties.class)
public class FileWatcherConfig {
    private final CsvFileAddListener csvFileAddListener;
    private EmployeeServiceProperties employeeServiceProperties;

    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        EmployeeServiceProperties.ImportProperties importProperties = employeeServiceProperties.getImporting();
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true,
                Duration.ofSeconds(importProperties.getWatchPollIntervalSeconds()),
                Duration.ofSeconds(importProperties.getWatchQuietPeriodSeconds()));

        fileSystemWatcher.addSourceDirectory(new File(importProperties.getWatchLocation()));
        fileSystemWatcher.addListener(csvFileAddListener);
        fileSystemWatcher.start();

        log.info("Start monitoring for csv files in {}", importProperties.getWatchLocation());
        return fileSystemWatcher;
    }

    @PreDestroy
    public void onDestroy() {
        fileSystemWatcher().stop();
    }

}
