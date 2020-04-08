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
    private Map<Student, Project> map;

    public SimulatedAnnealing(Solution solution, ProjectDAO projectDAO, StudentDAO studentDAO) {
        currentBest = solution;
        //Generate map of students to projects
        Map<Student, Project> map = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : solution.getSolution().entrySet()) {
            map.put(studentDAO.getOne(entry.getKey()), projectDAO.getOne(entry.getValue()));
        }
        this.map = map;
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
}
