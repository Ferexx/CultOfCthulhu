package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.Solution;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.HashMap;
import java.util.Map;

public class SimulatedAnnealing {
    private Solution currentBest;
    private ProjectDAO projectDAO;
    private StudentDAO studentDAO;

    public SimulatedAnnealing(Solution solution, ProjectDAO projectDAO, StudentDAO studentDAO) {
        currentBest = solution;
        this.projectDAO = projectDAO;
        this.studentDAO = studentDAO;
    }

    public double assessSolution(Solution solution) {
        double energy = 1;
        if(violatesHardConstraints(solution)) return energy;

        //Soft constraints code here
        return energy;
    }

    public boolean violatesHardConstraints(Solution solution) {
        /*Need to do a few checks here:
        Check if more than one person has the same project
        Check everyone has a project
        Self-specified project can only be assigned to that student
        Maybe more
         */
        return false;
    }

    public Map<Student, Project> createDirectMap(Map<Integer, Integer> map) {
        Map<Student, Project> newMap = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            newMap.put(studentDAO.getOne(entry.getKey()), projectDAO.getOne(entry.getValue()));
        }
        return newMap;
    }
}
