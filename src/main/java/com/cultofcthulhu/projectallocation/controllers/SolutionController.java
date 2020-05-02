package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.models.GeneticAlgorithmSolutionHerd;
import com.cultofcthulhu.projectallocation.models.Solution;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.solvers.GeneticAlgorithm;
import com.cultofcthulhu.projectallocation.solvers.SimulatedAnnealing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            model.addAttribute("list", simulation.hillClimb(GPARange, studentDAO, projectDAO).getSolutionList(studentDAO, projectDAO));
        }
        else {
            GeneticAlgorithmSolutionHerd solutionHerd = new GeneticAlgorithmSolutionHerd(initialSolution);
            GeneticAlgorithm genet = new GeneticAlgorithm(solutionHerd);
            solutionHerd.getSolution(0).setFitness(genet.assessSolution(solutionHerd.getSolution(0), GPARange, studentDAO, projectDAO));
            System.out.println(solutionHerd.getSolution(0).printSolution(studentDAO, projectDAO));
            model.addAttribute("list", genet.runAlgorithm(GPARange, studentDAO, projectDAO).getSolutionList(studentDAO,projectDAO));
        }
        return "solution";
    }

    @RequestMapping(value = "solutionDownload")
    public ResponseEntity downloadSolution() {
        Path path = Paths.get("user-files/" + "solution.txt");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/txt")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
