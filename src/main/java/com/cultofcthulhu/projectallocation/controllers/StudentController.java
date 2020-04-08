package com.cultofcthulhu.projectallocation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @PostMapping(value = "/preferences")
    public String preferencePage(@RequestParam long studentNum, Model model) {
        model.addAttribute("title", "Preferences");
        return "preferences";
    }
}
