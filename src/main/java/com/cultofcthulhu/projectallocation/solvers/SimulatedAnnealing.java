package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.Map;

public class SimulatedAnnealing {
    public Solution currentBest;
    public SimulatedAnnealing(Solution solution) {
        currentBest = solution;
    }

    public Solution hillClimb(double GPA_impact, StudentDAO studentDAO, ProjectDAO projectDAO) {
        int count = 0;
        do {
            Solution newSolution = new Solution(currentBest.change(1));
            newSolution.setEnergy(assessSolution(newSolution, GPA_impact, studentDAO, projectDAO));
            if (currentBest.getEnergy() > newSolution.getEnergy()) {
                currentBest = newSolution;
                count = 0;
                System.out.println(currentBest.getEnergy());
            } else count++;
        } while(count < Math.pow(60, 2));
        return currentBest;
    }

    public double assessSolution(Solution solution, double GPA_impact, StudentDAO studentDAO, ProjectDAO projectDAO) {
        double energy = 0;

        if(violatesHardConstraints(solution.getSolution())) energy += 100;

        //Main loop to iterate through all students, and the projects assigned to them
        for (Map.Entry<Integer, Integer> currentPair : solution.getSolution().entrySet()) {
            Student currentStudent = studentDAO.getOne(currentPair.getKey());
            //First, add energy based on how far down in each students preference list their assigned project is
            for(Map.Entry<Integer, Integer> preference : currentStudent.getPreferences().entrySet()) {
                //If the current preference is the project the student was assigned, add energy according to how far down the list we are
                if(preference.getValue().equals(currentPair.getValue())) energy += preference.getKey();
            }
            //If a student got a project not in their preferences list, add significantly more energy
            if(!currentStudent.getPreferences().containsValue(currentPair.getValue())) energy += 50;

            /* Next, check our GPA constraint:
            For each student, get the projects that they were not assigned, that were above the project they were assigned in their preference list.
            So if a student was assigned their 5th preference, find their 1st, 2nd, 3rd, and 4th preference. */
            Map<Integer, Integer> preferences = currentStudent.getPreferences();
            int assignedPreference = preferences.size();
            for(Map.Entry<Integer, Integer> preference : preferences.entrySet()) {
                if(preference.getValue().equals(currentPair.getValue()))
                    assignedPreference = preference.getKey();
            }
            /* Next, find the students that were assigned these projects. Check if their GPA is higher than our current student.
            If their GPA is lower than our current student, then this violates our GPA constraint, so add energy based on how much the user has chosen
            GPA to impact the outcome. */
            for(Map.Entry<Integer, Integer> preference : preferences.entrySet()) {
                int studentID = projectToStudent(preference.getValue());
                //We perform this check because there are more projects than students, so a student may not have been assigned this project
                if(studentID != -1) {
                    Student assignedStudent = studentDAO.getOne(studentID);
                    if (assignedStudent.getGpa() < currentStudent.getGpa()) {
                        energy += (10 * GPA_impact);
                    }
                }
                if(preference.getKey() + 1 == assignedPreference) break;
            }
        }
        return energy;
    }

    public boolean violatesHardConstraints(Map<Integer, Integer> map) {
        /*Need to do a few checks here:
        Check if more than one person has the same project
        Check everyone has a project
        Self-specified project can only be assigned to that student
        Maybe more
         */
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            //First check if a project has been assigned to more than one person
            for (Map.Entry<Integer, Integer> entry2 : map.entrySet())
                if (entry.getValue().equals(entry2.getValue()) && !entry.getKey().equals(entry2.getKey()))
                    return true;
            //Next check if a student has actually been assigned a project
            if(entry.getValue() == -1)
                return true;
        }
        return false;
    }

    public int projectToStudent(int id) {
        for(Map.Entry<Integer, Integer> entry : currentBest.getSolution().entrySet()) {
            if(entry.getValue() == id)
                return entry.getKey();
        }
        return -1;
    }
}
