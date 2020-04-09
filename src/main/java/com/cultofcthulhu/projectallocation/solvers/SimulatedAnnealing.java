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

    public double assessSolution(Solution solution, boolean constraint_GPA, double constraint_GPA_impact) {
        double energy = 0;

        Map<Student, Project> studentProjectMap = createDirectMap(solution.getSolution());

        for (Map.Entry<Student, Project> entry : studentProjectMap.entrySet()) {
            Map<Integer, Integer> entryPreferences = entry.getKey().getPreferences();
            for (int i = 0; i < entryPreferences.size() ; i++){
                if (entryPreferences.get(i) == entry.getValue().getId()){
                    energy = energy + i; //adds the place in the preference list to the total energy
                } else if (i == entryPreferences.size() && entryPreferences.get(i) != entry.getValue().getId()){ //if a student has a project not on their preference list increase energy
                    energy = energy + i + 1; //increase energy by 1 more then if they had gotten their last preference
                }
            }
        }



        if (constraint_GPA){
            int preference_number = 0;
            for (Map.Entry<Student, Project> entry : studentProjectMap.entrySet()){

                Map<Integer, Integer> entryPreferences = entry.getKey().getPreferences();
                preference_number = entryPreferences.size();

                for (int i = 0; i < entryPreferences.size() ; i++){
                    if (entryPreferences.get(i) == entry.getKey().getAssignedProjectID()){
                        preference_number = i;
                    }
                }

                for (int i = preference_number; i > 0 ; i--){
                    if (studentDAO.getOne(projectDAO.getOne(entryPreferences.get(i)).getStudentAssigned()).getGpa() < entry.getKey().getGpa()){
                        energy = energy + (10 * constraint_GPA_impact);
                    }
                }
            }
        }



        return energy;
    }

    public boolean violatesHardConstraints(Solution solution) {
        /*Need to do a few checks here:
        Check if more than one person has the same project
        Check everyone has a project
        Self-specified project can only be assigned to that student
        Maybe more
         */
        Map<Student, Project> map = createDirectMap(solution.getSolution());
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
