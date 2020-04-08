package com.cultofcthulhu.projectallocation.solvers;

import com.cultofcthulhu.projectallocation.models.data.Solution;

public class SimulatedAnnealing {
    public Solution currentBest;

    public SimulatedAnnealing(Solution solution) {
        currentBest = solution;
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
}
