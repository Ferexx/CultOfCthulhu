package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.interfaces.Solutionable;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class SolutionByLottery implements Solutionable {
    public void generateSolution(StudentDAO studentDAO, ProjectDAO projectDAO) {

        Integer[] student_project_assignment_order = new Integer[((int) studentDAO.count())];
        Boolean[] takenProjects = new Boolean[(int) projectDAO.count()];

        Arrays.fill(takenProjects, false);

        for (int i = 0; i < student_project_assignment_order.length; i++) { student_project_assignment_order[i] = i; }

        Collections.shuffle(Arrays.asList(student_project_assignment_order));
        System.out.println(Arrays.toString(student_project_assignment_order));

        for (int i = 0; i < student_project_assignment_order.length; i++) {
            Student student = studentDAO.getOne(student_project_assignment_order[i]);
            Map<Integer, Integer> preferences = student.getPreferences();

            for (int x = 0; x < preferences.size(); x++){
                if (!takenProjects[preferences.get(x)]){ //If project of preference x is not taken
                    student.setAssignedProjectID(preferences.get(x)); //Assign project to this student
                    projectDAO.getOne(preferences.get(x)).setStudentAssigned(student.getId()); //Assign student to project x
                    takenProjects[preferences.get(x)] = true; //record that project is now taken in this solution
                }

                if (x == preferences.size()){
                    //Need to write code that finds a free project if all preferences are taken
                }
            }
        }
    }
}
