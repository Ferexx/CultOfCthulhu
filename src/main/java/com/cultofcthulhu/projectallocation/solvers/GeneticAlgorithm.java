package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.HashMap;
import java.util.Map;

public class GeneticAlgorithm {
    public Solution currentBest;
    private ProjectDAO projectDAO;
    private StudentDAO studentDAO;

    public GeneticAlgorithm(Solution solution, ProjectDAO projectDAO, StudentDAO studentDAO) {
        currentBest = solution;
        this.projectDAO = projectDAO;
        this.studentDAO = studentDAO;
    }

    public double assessSolution(Solution solution, double GPA_impact) {
        double fitness = 5000;

        Map<Student, Project> studentProjectMap = createDirectMap(solution.getSolution());
        if(violatesHardConstraints(studentProjectMap)) fitness -= 100;

        //Main loop to iterate through all students, and the projects assigned to them
        for (Map.Entry<Student, Project> entry : studentProjectMap.entrySet()) {

            //First, decrease fitness based on how far down in each students preference list their assigned project is
            for(Map.Entry<Integer, Integer> preference : entry.getKey().getPreferences().entrySet()) {
                //The lower in the preference list something is, the more energy we add.
                //The preference list map starts at 0, so someone getting their first preference doesn't decrease fitness
                if(preference.getValue() == entry.getKey().getAssignedProjectID()) fitness += preference.getKey();
            }
            //If a student got a project not in their preferences list, significantly decrease fitness
            if(!entry.getKey().getPreferences().containsValue(entry.getKey().getAssignedProjectID())) fitness -= 50;

            /* Next, check our GPA constraint:
            For each student, get the projects that they were not assigned, that were above the project they were assigned in their preference list.
            So if a student was assigned their 5th preference, find their 1st, 2nd, 3rd, and 4th preference.
            Next, find the students that were assigned these projects. Check if their GPA is higher than our current student.
            If their GPA is lower than our current student, then this violates our GPA constraint, so decrease fitness based on how much the user has chosen
            GPA to impact the outcome.
             */
            Map<Integer, Integer> entryPreferences = entry.getKey().getPreferences();
            int preference_number = entryPreferences.size();
            for(int i = 0; i < entryPreferences.size(); i++) {
                if (entryPreferences.get(i) == entry.getKey().getAssignedProjectID())
                    preference_number = i;
            }
            for(int i = preference_number; i >= 0; i--) {
                if(studentDAO.getOne(projectDAO.getOne(entryPreferences.get(i)).getStudentAssigned()).getGpa() < entry.getKey().getGpa()) {
                    fitness -= (10 * GPA_impact);
                }
            }
        }
        return fitness;
    }

    public boolean violatesHardConstraints(Map<Student,Project> map) {
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
