package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.Solution;
import com.cultofcthulhu.projectallocation.models.data.SolutionDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.solvers.SolutionByLottery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SolutionController {
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private SolutionDAO solutionDAO;

    @RequestMapping(value = "/solutionByLottery")
    public String solutionByLottery(Model model) {
        model.addAttribute("title", "Solution by Lottery");
        SolutionByLottery lottery = new SolutionByLottery();
        lottery.generateSolution(studentDAO, projectDAO);
        model.addAttribute("map", generateMap());
        return "solution";
    }

    public Map<String, String> generateMap() {
        List<Student> students = studentDAO.findAll();
        Map<String, String> map = new HashMap<>();
        Map<Integer, Integer> solution = new HashMap<>();
        for (Student student : students) {
            map.put(student.getName(), projectDAO.findById(student.getAssignedProjectID()).get().getProject_title());
            solution.put(student.getId(), student.getAssignedProjectID());
        }
        solutionDAO.save(new Solution(solution));
        return map;
    }
}
