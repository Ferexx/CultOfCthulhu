package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.system.systemVariables;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {
    private Solution initialSolution;
    private List<Solution> solutionList;
    private StudentDAO studentDAO;
    private ProjectDAO projectDAO;

    public GeneticAlgorithm(Solution initialSolution) {
        this.initialSolution = initialSolution;
    }

    public Solution mate(Solution solution1, Solution solution2) {

        Integer[] currentSolutionOrder;
        Integer[] new_solution_order = new Integer[systemVariables.NUMBER_OF_STUDENTS];
        Arrays.fill(new_solution_order, 0);

        int place = -1;

        for (int student = 1 ; student <= systemVariables.NUMBER_OF_STUDENTS ; student++){
            if(student % 2 == 1) currentSolutionOrder = solution1.getStudent_project_assignment_order();
            else currentSolutionOrder = solution2.getStudent_project_assignment_order();

            for (int x = 0 ; x < systemVariables.NUMBER_OF_STUDENTS ; x++) {
                if (currentSolutionOrder[x] == student) {
                    place = x;
                    break;
                }
            }
            boolean placed = false;
            do {
                if (new_solution_order[place] == 0) {
                    new_solution_order[place] = student;
                    placed = true;
                } else {
                    place++;
                    if (place == new_solution_order.length){
                        for(int i=0; i + 1 < new_solution_order.length; i++) {
                            if (new_solution_order[i] == 0) {
                                new_solution_order[i] = new_solution_order[i+1];
                                new_solution_order[i] = 0;
                            }
                        }
                    }
                }
            } while (!placed);
        }

        Solution returnSolution = new Solution(solution1.getSolution(), new_solution_order);
        returnSolution.generateSolution(studentDAO, projectDAO);
        return returnSolution;
    }

    public Solution mutate(Solution solution) {
        if(ThreadLocalRandom.current().nextDouble(0, 1) <= systemVariables.MUTATION_CHANCE)
            solution.change();
        return solution;
    }

    public double assessSolution(Solution solution, double GPA_impact, StudentDAO studentDAO, ProjectDAO projectDAO) {
        double fitness = 0;

        if (violatesHardConstraints(solution.getSolution())) {return 0;}

        for(Map.Entry<Integer,Integer> currentPair : solution.getSolution().entrySet()) {
            Student currentStudent = studentDAO.getOne(currentPair.getKey());
            //First, add fitness based on how far down in each student's preference list their assigned project is
            //For each project that they weren't assigned, find the student that was assigned to it, and decrease fitness based on whether their GPA is lower than our current student's
            for(Map.Entry<Integer,Integer> preference : currentStudent.getPreferences().entrySet()) {
                if(preference.getValue().equals(currentPair.getValue())) fitness += systemVariables.NUMBER_OF_PREFERENCES - preference.getKey();
                else {
                    int studentID = projectToStudent(preference.getValue(), solution.getSolution());
                    if(studentID != -1) {
                        Student assignedStudent = studentDAO.getOne(studentID);
                        if(assignedStudent.getGpa() < currentStudent.getGpa())
                            fitness -= (10 * GPA_impact);
                    }
                }
            }
            if(!currentStudent.getPreferences().containsValue(currentPair.getValue())) fitness -= 50;
        }
        if(fitness < 0) fitness = 0;
        fitness /= systemVariables.NUMBER_OF_STUDENTS;
        return fitness;
    }

    public boolean violatesHardConstraints(Map<Integer, Integer> map) {
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

    public int projectToStudent(int id, Map<Integer,Integer> solution) {
        for(Map.Entry<Integer, Integer> entry : solution.entrySet()) {
            if(entry.getValue() == id)
                return entry.getKey();
        }
        return -1;
    }
}