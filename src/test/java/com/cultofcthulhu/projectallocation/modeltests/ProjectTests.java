package com.cultofcthulhu.projectallocation.modeltests;

import com.cultofcthulhu.projectallocation.models.Project;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTests {

    @Test
    public void testConstructor() {
        Project project = new Project("App for destruction", 0, "CS+DS");

        assertEquals("App for destruction", project.getProject_title());
        assertEquals(0, project.getProposed_by());
        assertEquals("CS+DS", project.getSuitable_for_streams());
    }

    @Test
    public void testToString() {
        Project project = new Project("App for destruction", 0, "CS+DS");

        assertEquals("0,App for destruction,CS+DS", project.toString());
    }
}
