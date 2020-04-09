package com.cultofcthulhu.projectallocation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @PostMapping(value = "/preferences")
    public String preferencePage(@RequestParam long studentNum, Model model) {
        model.addAttribute("title", "Preferences");
        return "preferences";
    }

    @RequestMapping(value = "/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Log In");
        return "login";
    }

    @RequestMapping(value = "/students")
    public String studentPage(Model model) {
        model.addAttribute("title", "Student");
        return "students";
    }

    @RequestMapping(value = "/about")
    public String aboutPage(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }

    @RequestMapping(value = "/index")
    public String indexPage(Model model) {
        model.addAttribute("title", "Home");
        return "index";
    }
}
