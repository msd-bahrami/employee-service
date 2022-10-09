package net.identrics.employeeservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CsvFileAddListenerTest {

    public static final String ANY_CSV_FILE_NAME = "test.csv";
    @InjectMocks
    CsvFileAddListener csvFileAddListener;

    @Mock
    FileWatcherService fileWatcherService;

    @Mock
    ChangedFiles changedFiles;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ChangedFile changedFile;

    @BeforeEach
    void setUp() {
        when(changedFiles.getFiles()).thenReturn(Set.of(changedFile));
    }

    @Test
    @DisplayName("Given not added changed files When OnChange Then should not call service")
    void given_notAddedChangedFiles_whenOnChange_then_shouldNotCallService() {
        when(changedFile.getType()).thenReturn(ChangedFile.Type.DELETE);

        csvFileAddListener.onChange(Set.of(changedFiles));

        verify(fileWatcherService, never()).loadAndSaveCSVFile(any());
    }

    @Test
    @DisplayName("Given added changed CSV files When OnChange Then should call service")
    void given_AddedChangedCsvFiles_whenOnChange_then_shouldNotCallService() {
        when(changedFile.getType()).thenReturn(ChangedFile.Type.ADD);
        when(changedFile.getFile().getName()).thenReturn(ANY_CSV_FILE_NAME);

        csvFileAddListener.onChange(Set.of(changedFiles));

        verify(fileWatcherService).loadAndSaveCSVFile(any());
    }
}