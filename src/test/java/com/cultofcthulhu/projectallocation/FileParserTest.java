package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import org.junit.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileParserTest {

    @Test
    public void testParse() throws ParseException {
        FileParser csvWorks = new FileParser(new File("test-files/ShouldWork.csv"));
        FileParser tsvWorks = new FileParser(new File("test-files/ShouldWork.tsv"));

        Exception exception = assertThrows(ParseException.class, () -> {
            FileParser parser = new FileParser(new File("test-files/WontWork.csv"));
        });
        String expectedMessage = "Your file has an incorrect number of fields";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
