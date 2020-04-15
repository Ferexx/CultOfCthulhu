package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.interfaces.Solutionable;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.util.*;

public class SolutionByLottery implements Solutionable {
    public SolutionByLottery() {}
    public Solution generateSolution(StudentDAO studentDAO, ProjectDAO projectDAO) {

        Integer[] student_project_assignment_order = new Integer[((int) studentDAO.count())];
        Boolean[] takenProjects = new Boolean[(int) projectDAO.count()];

        Map<Integer, Integer> solution = new HashMap<>();

        Arrays.fill(takenProjects, false);

        for (int i = 0; i < student_project_assignment_order.length; i++) { student_project_assignment_order[i] = i; }

        Collections.shuffle(Arrays.asList(student_project_assignment_order));

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
                        student.setAssignedProjectID(x);
                        Project project = projectDAO.getOne(x);
                        project.setStudentAssigned(student.getId());
                        projectDAO.save(project);
                        takenProjects[x] = true;
                        solution.put(student.getId(), project.getId());
                        break;
                    }
                }
            }
        }
        return new Solution(solution);
    }
}
