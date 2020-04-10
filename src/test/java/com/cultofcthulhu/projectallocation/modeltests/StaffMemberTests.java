package com.cultofcthulhu.projectallocation.modeltests;

import com.cultofcthulhu.projectallocation.models.StaffMember;
import org.junit.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaffMemberTests {

    @Test
    public void testConstructor() {
        StaffMember staffMember = new StaffMember("Jack Price", Map.ofEntries(entry(0, "Gaming"), entry(1, "Testing"), entry(2, "Not sleeping")), "Dagon Studies");

        assertEquals("Jack Price", staffMember.getName());
        assertEquals(Map.ofEntries(entry(0, "Gaming"), entry(1, "Testing"), entry(2, "Not sleeping")), staffMember.getResearch_interests());
        assertEquals("Dagon Studies", staffMember.getStream());
    }

}
