package net.identrics.employeeservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.identrics.employeeservice.entity.Employee;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
@AllArgsConstructor
public class FileWatcherService {
    public static final String CLASSPATH_INIT_CSV = "classpath:init/*.csv";
    private final EmployeeService employeeService;
    private final ResourcePatternResolver resourcePatternResolver;

    @PostConstruct
    @Transactional
    public void init() throws IOException {
        log.info("Initializing database from {}", CLASSPATH_INIT_CSV);
        Resource[] resources = getCSVResources(CLASSPATH_INIT_CSV);

        for (Resource resource : resources) {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            importCsv(inputStreamReader);
        }
    }

    @Transactional
    void importCsv(InputStreamReader inputStreamReader) {
        CsvReader csvReader = new CsvReader(inputStreamReader);

        while (csvReader.hasNext()) {
            String[] row = csvReader.readNext();
            employeeService.save(rowToEmployee(row));
        }
    }

    private Employee rowToEmployee(String[] row) {
        Employee employee = new Employee();
        employee.setName(row[0]);
        employee.setCompany(row[1]);
        employee.setEducation(row[2]);
        employee.setSalary(Long.parseLong(row[3]));
        return employee;
    }

    public void loadAndSaveCSVFile(ChangedFile changedFile) {
        log.info("Importing {} ...", changedFile.getFile().getName());
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(changedFile.getFile()));
            importCsv(inputStreamReader);
            inputStreamReader.close();
            log.info("{} imported.", changedFile.getFile().getName());
        } catch (IOException e) {
            log.error("Error in importing " + changedFile.getFile().getName(), e);
        }
    }

    private Resource[] getCSVResources(String locationPattern) throws IOException {
        return resourcePatternResolver.getResources(locationPattern);
    }

}
