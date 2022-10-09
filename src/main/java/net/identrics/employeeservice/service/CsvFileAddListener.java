package net.identrics.employeeservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.springframework.boot.devtools.filewatch.ChangedFile.Type;

@Component
@AllArgsConstructor
public class CsvFileAddListener implements FileChangeListener {

    public static final String CSV_SUFFIX = ".csv";
    private final FileWatcherService fileWatcherService;

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for (ChangedFiles files : changeSet) {
            for (ChangedFile file : files.getFiles()) {
                if (Type.ADD.equals(file.getType()) && file.getFile().getName().toLowerCase().endsWith(CSV_SUFFIX)) {
                    fileWatcherService.loadAndSaveCSVFile(file);
                }
            }
        }
    }

}
