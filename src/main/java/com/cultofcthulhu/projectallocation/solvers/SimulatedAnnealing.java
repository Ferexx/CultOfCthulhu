package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.Solution;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.HashMap;
import java.util.Map;

public class SimulatedAnnealing {
    public Solution currentBest;
    private ProjectDAO projectDAO;
    private StudentDAO studentDAO;

    public SimulatedAnnealing(Solution solution, ProjectDAO projectDAO, StudentDAO studentDAO) {
        currentBest = solution;
        this.projectDAO = projectDAO;
        this.studentDAO = studentDAO;
    }

    public double assessSolution(Solution solution, int constraint_GPA) {
        double energy = 0;

        Map<Student, Project> studentProjectMap = createDirectMap(solution.getSolution());
        if(violatesHardConstraints(studentProjectMap)) energy += 100;

        //Main loop to iterate through all students, and the projects assigned to them
        for (Map.Entry<Student, Project> entry : studentProjectMap.entrySet()) {

            //First, add energy based on how far down in each students preference list their assigned project is
            for(Map.Entry<Integer, Integer> preference : entry.getKey().getPreferences().entrySet()) {
                //The lower in the preference list something is, the more energy we add.
                //The preference list map starts at 0, so someone getting their first preference adds 0 energy.
                if(preference.getValue() == entry.getKey().getAssignedProjectID()) energy += preference.getKey();
            }
            //If a student got a project not in their preferences list, add significantly more energy
            if(!entry.getKey().getPreferences().containsValue(entry.getKey().getAssignedProjectID())) energy += 50;

            //Next, add energy based on whether students with a higher GPA got preferences that were higher in their list
        }

        return energy;
    }

    public boolean violatesHardConstraints(Map<Student, Project> map) {
        /*Need to do a few checks here:
        Check if more than one person has the same project
        Check everyone has a project
        Self-specified project can only be assigned to that student
        Maybe more
         */
        for(Map.Entry<Student, Project> entry : map.entrySet()) {
            //First check if a project has been assigned to more than one person
            for (Map.Entry<Student, Project> entry2 : map.entrySet())
                if (entry.getKey().getAssignedProjectID() == entry2.getKey().getAssignedProjectID() && !entry.getKey().equals(entry2.getKey()))
                    return true;
            //Next check if a student has actually been assigned a project
            if(entry.getKey().getAssignedProjectID() == -1)
                return true;
        }
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
