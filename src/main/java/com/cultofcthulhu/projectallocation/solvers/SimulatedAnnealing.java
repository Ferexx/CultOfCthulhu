package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.data.Solution;

public class SimulatedAnnealing {
<<<<<<< HEAD
    public Solution currentBest;
=======
    private Solution currentBest;
    private ProjectDAO projectDAO;
    private StudentDAO studentDAO;
>>>>>>> a06ec6abf9dcf60043a4f27ccbc482e9b7578baa

    public SimulatedAnnealing(Solution solution) {
        currentBest = solution;
<<<<<<< HEAD
=======
        this.projectDAO = projectDAO;
        this.studentDAO = studentDAO;
>>>>>>> a06ec6abf9dcf60043a4f27ccbc482e9b7578baa
    }

    public double assessSolution(Solution solution) {
        double energy = 1;
        if(violatesHardConstraints(solution)) return energy;

        //Soft constraints code here
        return energy;
    }

    public boolean violatesHardConstraints(Solution solution) {
        /*Need to do a few checks here:
        Check if more than one person has the same project
        Check everyone has a project
        Self-specified project can only be assigned to that student
        Maybe more
         */
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
