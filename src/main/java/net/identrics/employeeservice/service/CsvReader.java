package net.identrics.employeeservice.service;

import liquibase.util.csv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class CsvReader {
    private final CSVReader csvReaderTool;
    private String[] next;

    public CsvReader(InputStreamReader inputStream) {
        this.csvReaderTool = new CSVReader(inputStream);
        skipHeader();
    }

    private void skipHeader() {
        this.hasNext();
    }

    @Nullable
    public String[] readNext() {
        return next;
    }

    public boolean hasNext() {
        try {
            next = csvReaderTool.readNext();
            return next != null && next.length > 0;
        } catch (IOException e) {
            log.warn("Error in reading next line", e);
            return false;
        }
    }
}
