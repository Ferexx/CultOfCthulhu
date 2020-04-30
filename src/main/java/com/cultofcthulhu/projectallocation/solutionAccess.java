package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import java.io.FileWriter;

public class solutionAccess {
    solutionAccess(){}

    public void solutionSave(Solution solution, String fileName) throws Exception{
        Integer[] student_project_assignment_order = solution.getStudent_project_assignment_order();
        FileWriter writer = new FileWriter("files/" + fileName + ".txt");

        for (Integer integer : student_project_assignment_order) {
            writer.write(integer.toString() + "\n");
        }
    }

    public void solutionLoad(ProjectDAO projectDAO, StudentDAO studentDAO){
        
    }
}
