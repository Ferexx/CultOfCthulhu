package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.SolutionDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.solvers.GeneticAlgorithm;
import com.cultofcthulhu.projectallocation.solvers.SimulatedAnnealing;
import com.cultofcthulhu.projectallocation.solvers.SolutionByLottery;
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
    @Autowired
    private SolutionDAO solutionDAO;

    @PostMapping(value = "/solution")
    public String solution(@RequestParam double GPARange, @RequestParam String choice, Model model) {
        SolutionByLottery lottery = new SolutionByLottery(studentDAO);
        if(choice.equals("simulatedAnnealing")) {
            model.addAttribute("title", "Simulated Annealing");
            SimulatedAnnealing simulation = new SimulatedAnnealing(lottery.generateSolution(studentDAO, projectDAO));
            simulation.currentBest.setEnergy(simulation.assessSolution(simulation.currentBest, GPARange, studentDAO, projectDAO));
            model.addAttribute("map", generateMap(simulation.hillClimb(GPARange, studentDAO, projectDAO).getSolution()));
        }
        else {
            GeneticAlgorithm genet = new GeneticAlgorithm(lottery.generateSolution(studentDAO, projectDAO));
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
