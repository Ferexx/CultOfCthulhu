package com.cultofcthulhu.projectallocation.modeltests;

import com.cultofcthulhu.projectallocation.models.Student;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentTests {

    @Test
    public void testConstructor() {
        Student student = new Student("Jack", "Price", "Dagon Studies");

        assertEquals("Jack", student.getFirstName());
        assertEquals("Price", student.getLastName());
        assertEquals("Jack Price", student.getName());
        assertEquals("Dagon Studies", student.getStream());
    }


}
