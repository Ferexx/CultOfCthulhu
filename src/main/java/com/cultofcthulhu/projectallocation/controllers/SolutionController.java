package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.models.GeneticAlgorithmSolutionHerd;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.solvers.GeneticAlgorithm;
import com.cultofcthulhu.projectallocation.solvers.SimulatedAnnealing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SolutionController {
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private ProjectDAO projectDAO;

    @PostMapping(value = "/solution")
    public String solution(@RequestParam double GPARange, @RequestParam String choice, Model model) {
        Solution initialSolution = new Solution(studentDAO, projectDAO);
        if(choice.equals("simulatedAnnealing")) {
            model.addAttribute("title", "Simulated Annealing");
            SimulatedAnnealing simulation = new SimulatedAnnealing(initialSolution);
            simulation.currentBest.setEnergy(simulation.assessSolution(simulation.currentBest, GPARange, studentDAO, projectDAO));
            model.addAttribute("map", generateMap(simulation.hillClimb(GPARange, studentDAO, projectDAO).getSolution()));
        }
        else {
            GeneticAlgorithmSolutionHerd solutionHerd = new GeneticAlgorithmSolutionHerd(initialSolution);
            GeneticAlgorithm genet = new GeneticAlgorithm(solutionHerd);
            solutionHerd.getSolution(0).setFitness(genet.assessSolution(solutionHerd.getSolution(0), GPARange, studentDAO, projectDAO));
            System.out.println(solutionHerd.getSolution(0).printSolution(studentDAO, projectDAO));
            model.addAttribute("map", generateMap(genet.runAlgorithm(GPARange, studentDAO, projectDAO)));
        }
        return "solution";
    }

    public Map<String, String> generateMap(Map<Integer, Integer> solutionMap) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry: solutionMap.entrySet()) {
            Student student = studentDAO.getOne(entry.getKey());
            Project project = projectDAO.getOne(entry.getValue());
            map.put(student.getName(), project.getProject_title());
        }
        return map;
    }

}
