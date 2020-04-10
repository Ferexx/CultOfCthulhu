package com.cultofcthulhu.projectallocation.modeltests;

import com.cultofcthulhu.projectallocation.models.Solution;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SolutionTests {

    @Test
    public void testConstructor() {
        Solution solution = new Solution(Map.ofEntries(entry(0,0),entry(1,1),entry(2,2)));

        assertEquals(Map.ofEntries(entry(0,0),entry(1,1),entry(2,2)), solution.getSolution());
    }

    @Test
    public void testChange() {
        Solution solution = new Solution(Map.ofEntries(entry(0,0),entry(1,1),entry(2,2),entry(3,3),entry(4,4),entry(5,5),entry(6,6),entry(7,7)));

        assertNotEquals(solution.getSolution(), solution.change(1));
        assertNotEquals(solution.getSolution(), solution.change(2));
        assertNotEquals(solution.getSolution(), solution.change(3));
    }
}
