package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.system.systemVariables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class solutionAccess {
    solutionAccess(){}

    public void solutionSaveToFile(Solution solution, String fileName) throws Exception{
        Integer[] student_project_assignment_order = solution.getStudent_project_assignment_order();
        FileWriter writer = new FileWriter("files/" + fileName + ".txt");

        for (Integer integer : student_project_assignment_order) {
            writer.write(integer.toString() + "\n");
        }
    }

    public Solution solutionLoadFromFile(String fileName, ProjectDAO projectDAO, StudentDAO studentDAO) throws FileNotFoundException {
        Solution output;
        Scanner scanner = new Scanner(new File(fileName));
        Integer[] student_project_assignment_order = new Integer[systemVariables.NUMBER_OF_STUDENTS];
        int i = 0;
        while(scanner.hasNextInt()){
            student_project_assignment_order[i++] = scanner.nextInt();
        }
        
        output = new Solution(student_project_assignment_order);
        output.generateSolution(studentDAO, projectDAO);

        return output;
    }
}
