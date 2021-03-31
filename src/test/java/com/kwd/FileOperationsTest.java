package com.kwd;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileOperationsTest {

    public static final String WELL_KNOWN_RECORD_FILE_NAME = "typing records.csv";

    @Test
    void returns_record_file_with_well_known_name() throws Exception {
        File recordFile = FileOperations.getRecordFile();

        assertEquals(WELL_KNOWN_RECORD_FILE_NAME, recordFile.getName());
    }

    @Test
    void returns_created_record_file_when_none_exists() throws Exception {
        Files.deleteIfExists(Path.of(WELL_KNOWN_RECORD_FILE_NAME));

        File recordFile = FileOperations.getRecordFile();

        assertTrue(recordFile.exists());
    }

    @Test
    void returns_record_file_when_it_exists_without_changes_to_content() throws Exception {
        try (Writer writer = new FileWriter(WELL_KNOWN_RECORD_FILE_NAME, false)) {
            writer.write("some content");
        }

        File recordFile = FileOperations.getRecordFile();

        BufferedReader reader = new BufferedReader(new FileReader(recordFile));
        String contents = reader.readLine();
        assertEquals("some content", contents);
    }
}
