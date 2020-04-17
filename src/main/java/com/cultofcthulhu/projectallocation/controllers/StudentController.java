package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.system.systemVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StudentController {

    @Autowired
    private ProjectDAO projectDAO;

    @PostMapping(value = "/preferences")
    public String preferencePage(@RequestParam long studentNum, Model model) {
        model.addAttribute("title", "Preferences");
        model.addAttribute("projects", projectDAO.findAll());
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

    @RequestMapping(value = "/settings")
    public String settingsPage(Model model) {
        model.addAttribute("title", "Settings");
        model.addAttribute("numStudents", systemVariables.NUMBER_OF_STUDENTS);
        model.addAttribute("numPreferences", systemVariables.NUMBER_OF_PREFERENCES);
        return "settings";
    }

    @PostMapping(value = "/updateSettings")
    public String updateSettings(@RequestParam("noOfStudents") int students, @RequestParam("noOfPreferences") int preferences, HttpServletRequest request) {
        systemVariables.NUMBER_OF_STUDENTS = students;
        systemVariables.NUMBER_OF_PREFERENCES = preferences;
        return "redirect:" + request.getHeader("Referer");
    }
}
