package com.cultofcthulhu.projectallocation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GenerationController {

    @RequestMapping(value = "/howManyStudents")
    public String howManyStudents(Model model) {
        model.addAttribute("title", "Number of Students");
        return "studentNums";
    }

    @PostMapping(value = "numStudents")
    public void numStudents(@RequestParam("number") Integer number) {
        generateProjects(number);
    }

    public void generateProjects(int number) {
        //TODO: Generate projects based on how many students there are
    }
}
