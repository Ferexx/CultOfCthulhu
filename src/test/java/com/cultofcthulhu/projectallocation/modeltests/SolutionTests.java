package com.cultofcthulhu.projectallocation.modeltests;

import com.cultofcthulhu.projectallocation.models.Solution;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SolutionTests {

    @Test
    public void testConstructor() {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            map.put(i, i);
        }
        Solution solution = new Solution(map);

        assertEquals(map, solution.getSolution());
    }

    @Test
    public void testChange() {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            map.put(i, i);
        }
        Solution solution = new Solution(map);

        assertNotEquals(solution.getSolution().toString(), solution.change(1).toString());
        assertNotEquals(solution.getSolution().toString(), solution.change(2).toString());
        assertNotEquals(solution.getSolution().toString(), solution.change(3).toString());
    }
}
