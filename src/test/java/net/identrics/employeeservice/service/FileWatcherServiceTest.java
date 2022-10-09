package net.identrics.employeeservice.service;

import net.identrics.employeeservice.config.EmployeeServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class FileWatcherServiceTest {

    @InjectMocks
    FileWatcherService fileWatcherService;

    @Mock
    EmployeeService employeeService;
    @Mock
    ResourcePatternResolver resourcePatternResolver;
    @Mock
    EmployeeServiceProperties employeeServiceProperties;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ChangedFile changedFile;

    @Value("classpath:sample.csv")
    Resource csvResource;


    @BeforeEach
    void setUp() throws IOException {
        when(changedFile.getFile().getName()).thenReturn(csvResource.getFilename());
        when(changedFile.getFile()).thenReturn(csvResource.getFile());
    }

    @Test
    @DisplayName("When loadAndSaveCSVFile Then should call save")
    void when_loadAndSaveCSVFile_then_shouldCallSave() {

        fileWatcherService.loadAndSaveCSVFile(changedFile);

        verify(employeeService, times(3)).save(any());

    }
}