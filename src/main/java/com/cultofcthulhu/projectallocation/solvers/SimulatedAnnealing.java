package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.interfaces.Solverable;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.system.systemVariables;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatedAnnealing implements Solverable {
    public Solution currentBest;
    int temperature = 100;
    public SimulatedAnnealing(Solution solution) {
        currentBest = solution;
    }

    public Solution hillClimb(double GPA_impact, StudentDAO studentDAO, ProjectDAO projectDAO) {
        int count = 0;
        Solution newSolution;
        do {
            do {
                newSolution = new Solution(currentBest.getSolution(), currentBest.getStudent_project_assignment_order());
                newSolution.change();
                newSolution = newSolution.assignProjects(studentDAO, projectDAO);
                newSolution.setEnergy(assessSolution(newSolution, GPA_impact, studentDAO, projectDAO));
                if (isAcceptable(newSolution)) {
                    currentBest = newSolution;
                    System.out.println("Accepted new solution with energy: " + currentBest.getEnergy());
                } else {
                    System.out.println("Rejected new solution with energy: " + newSolution.getEnergy());
                }
                count++;
            }while(count < systemVariables.NUMBER_OF_STUDENTS);
            //Cooling Schedule
            temperature /= 2;
            count = 0;
        } while(temperature > 1);

        System.out.println(currentBest.printSolution(studentDAO, projectDAO));

        return currentBest;
    }

    public double assessSolution(Solution solution, double GPA_impact, StudentDAO studentDAO, ProjectDAO projectDAO) {
        double energy = 0;

        if(violatesHardConstraints(solution.getSolution())) { energy += 100; }

        //Main loop to iterate through all students, and the projects assigned to them
        for (Map.Entry<Integer, Integer> currentPair : solution.getSolution().entrySet()) {
            Student currentStudent = studentDAO.getOne(currentPair.getKey());
            //First, add energy based on how far down in each students preference list their assigned project is
            //For each project they weren't assigned, find the student that was assigned to it, and check if their GPA is less than our current student's
            for(Map.Entry<Integer, Integer> preference : currentStudent.getPreferences().entrySet()) {
                //If the current preference is the project the student was assigned, add energy according to how far down the list we are
                if(preference.getValue().equals(currentPair.getValue())) energy += preference.getKey();
                else {
                    int studentID = projectToStudent(preference.getValue());
                    if (studentID != -1) {
                        Student assignedStudent = studentDAO.getOne(studentID);
                        if(assignedStudent.getGpa() < currentStudent.getGpa())
                            energy += (10 * GPA_impact);
                    }
                }
            }
            //If a student got a project not in their preferences list, add significantly more energy
            if(!currentStudent.getPreferences().containsValue(currentPair.getValue())) energy += 50;
        }

        energy = energy/systemVariables.NUMBER_OF_STUDENTS;
        energy = energy * 10;
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
                if (entry.getValue().equals(entry2.getValue()) && !entry.getKey().equals(entry2.getKey())) {
                    System.out.println("Project assigned to more than one student");
                    return true;
                }
            //Next check if a student has actually been assigned a project
            if(entry.getValue() == -1) {
                System.out.println("Student not assigned a project");
                return true;
            }
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

    public boolean isAcceptable(Solution newSolution) {
        double energyDifference = newSolution.getEnergy() - currentBest.getEnergy();
        if(energyDifference <= 0)
            return true;
        else {
            double probability = 1 / Math.exp(energyDifference/temperature);
            double accept = ThreadLocalRandom.current().nextDouble(0, 1);
            return accept < probability;
        }
    }
}
