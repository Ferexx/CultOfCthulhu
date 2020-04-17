package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileParserTest {

    @Test
    public void testParse() throws ParseException, IOException {
        FileParser StaffCsvWorks = new FileParser(new File("test-files/Staff/ShouldWork.csv"));
        FileParser StaffTsvWorks = new FileParser(new File("test-files/Staff/ShouldWork.tsv"));

        Exception exception = assertThrows(ParseException.class, () -> {
            FileParser parser = new FileParser(new File("test-files/Staff/WontWork.csv"));
        });
        String expectedMessage = "Your file has an incorrect number of fields";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        FileParser StudentParsingWorks = new FileParser();
        StudentParsingWorks.parseStudents(new File("test-files/Student/ShouldWork.csv"));
        StudentParsingWorks.parseStudents(new File("test-files/Student/ShouldWork.tsv"));

        Exception exception1 = assertThrows(ParseException.class, () -> {
            StudentParsingWorks.parseStudents(new File("test-files/Student/IncorrectFields.csv"));
        });
        String actualMessage1 = exception1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage));
        Exception exception2 = assertThrows(ParseException.class, () -> {
            StudentParsingWorks.parseStudents(new File("test-files/Student/IncorrectNumberOfPreferences.csv"));
        });
        String actualMessage2 = exception2.getMessage();
        assertTrue(actualMessage2.contains("does not have the correct number of preferences"));
        assertThrows(NumberFormatException.class, () -> {
            StudentParsingWorks.parseStudents(new File("test-files/Student/IncorrectPreferenceType.csv"));
        });
    }
}
