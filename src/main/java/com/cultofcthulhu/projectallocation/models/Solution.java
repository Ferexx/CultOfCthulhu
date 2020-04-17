package com.cultofcthulhu.projectallocation.models;

import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import javax.persistence.*;
import java.util.*;

@Entity
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    private Map<Integer, Integer> solution;
    private Integer[] student_project_assignment_order;
    private double energy;
    private double fitness;

    public int getId() {
        return id;
    }

    //public Solution(Map<Integer, Integer> map) {
    //    solution = new HashMap<>(map);
    //}

    public Solution(Map<Integer, Integer> map, Integer[] student_project_assignment_order)
    {
        this.student_project_assignment_order = student_project_assignment_order;
        solution = new HashMap<>(map);
    }

    public Solution(Map<Integer, Integer> map, double energy) {
        solution = new HashMap<>(map);
        this.energy = energy;
        fitness = -energy;
    }


    public Solution() {}

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setStudent_project_assignment_order(Integer[] student_project_assignment_order) {
        this.student_project_assignment_order = student_project_assignment_order;
    }

    public Integer[] getStudent_project_assignment_order() {
        return student_project_assignment_order;
    }

    public Map<Integer, Integer> getSolution() {
        return solution;
    }

    public void change(int degree) {
        Random rand = new Random();

        int x = rand.nextInt(student_project_assignment_order.length);
        int y = rand.nextInt(student_project_assignment_order.length);
        Collections.swap(Arrays.asList(student_project_assignment_order),x,y);
    }

    public Solution assignProjects(StudentDAO studentDAO, ProjectDAO projectDAO) {

        Map<Integer, Integer> solution = new HashMap<>();

        Boolean[] takenProjects = new Boolean[(int) projectDAO.count()];
        Arrays.fill(takenProjects, false);

        for (Integer integer : student_project_assignment_order) {
            Student student = studentDAO.getOne(integer+1);
            Map<Integer, Integer> preferences = student.getPreferences();

            for (int x = 0; x < preferences.size(); x++) {
                int preferenceX = preferences.get(x);
                if (!takenProjects[preferenceX]) { //If project of preference x is not taken
                    student.setAssignedProjectID(preferenceX); //Assign project to this student
                    Project project = projectDAO.getOne(preferenceX);
                    project.setStudentAssigned(student.getId()); //Assign student to project x
                    projectDAO.save(project);
                    takenProjects[preferenceX] = true; //record that project is now taken in this solution
                    solution.put(student.getId(), project.getId());
                    break; //Exit loop since we've assigned a student their highest preference possible
                }
            }

            //If we haven't assigned a student a project in the loop, just give them the first one that isn't taken
            if(student.getAssignedProjectID() == -1) {
                for(int x = 0; x < takenProjects.length; x++) {
                    if(!takenProjects[x]) {
                        student.setAssignedProjectID(x + 1);
                        Project project = projectDAO.getOne(x + 1);
                        project.setStudentAssigned(student.getId());
                        projectDAO.save(project);
                        takenProjects[x] = true;
                        solution.put(student.getId(), project.getId());
                        break;
                    }
                }
            }
        }
        return new Solution(solution, student_project_assignment_order);
    }
}
