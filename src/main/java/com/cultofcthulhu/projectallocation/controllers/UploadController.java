package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.FileParser;
import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StaffMemberDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class UploadController {

    private final StorageService storageService;
    public static FileParser parser = new FileParser();

    @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public StudentDAO studentDAO;

    @Autowired
    public StaffMemberDAO staffMemberDAO;

    @Autowired
    public ProjectDAO projectDAO;

    @RequestMapping(value = "")
    @GetMapping("/")
    public String tutorialPage(Model model) {
        model.addAttribute("title", "Welcome");
        return "index";
    }

    @RequestMapping(value = "/upload")
    public String uploadPage(Model model) {
        model.addAttribute("title", "File Upload");
        return "uploadFiles";
    }

    @PostMapping(value = "/uploadFiles")
    public String fileUpload(@RequestParam("staffFile") MultipartFile staffFile, @RequestParam("studentFile") MultipartFile studentFile, @RequestParam("projectFile") MultipartFile projectFile, Model model) {
        storageService.store(staffFile);
        storageService.store(studentFile);
        storageService.store(projectFile);
        File staff = new File(String.valueOf(storageService.load(staffFile.getOriginalFilename())));
        File student = new File(String.valueOf(storageService.load(studentFile.getOriginalFilename())));
        File project = new File(String.valueOf(storageService.load(projectFile.getOriginalFilename())));
        try {
            List<StaffMember> staffMembers = parser.parseStaff(staff);
            for(StaffMember currStaff : staffMembers)
                staffMemberDAO.save(currStaff);
            List<Student> students = parser.parseStudents(student);
            for(Student currStudent : students)
                studentDAO.save(currStudent);
            List<Project> projects = parser.parseProjects(project);
            for(Project currProject : projects)
                projectDAO.save(currProject);
            model.addAttribute("title", "Options");
            model.addAttribute("value", 0);
            return "options";
        } catch (ParseException | IOException | NumberFormatException e) {
            if(e.getClass() == ParseException.class)
                model.addAttribute("error", e.getMessage());
            else if(e.getClass() == IOException.class)
                model.addAttribute("error", "Internal error, please restart the process.");
            else if(e.getClass() == NumberFormatException.class)
                model.addAttribute("error", "Please ensure your preferences are stored as integers corresponding to project IDs, and GPA is stored as an integer or double");
            return "error";
        }
    }

    @GetMapping("/user-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
