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
        switch (number) {
            case 60:
                return;
            case 120:
                return;
            case 240:
                return;
            case 500:
                return;
        }

    }
}
