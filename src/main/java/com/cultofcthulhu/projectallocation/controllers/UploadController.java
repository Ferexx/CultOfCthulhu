package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.FileParser;
import com.cultofcthulhu.projectallocation.RandomGenerator;
import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StaffMemberDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.storage.StorageFileNotFoundException;
import com.cultofcthulhu.projectallocation.storage.StorageService;
import com.cultofcthulhu.projectallocation.system.systemVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    private final StorageService storageService;
    public static FileParser parser;

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
    public String listUploadedFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "index";
    }

    @GetMapping("/user-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping(value = "/uploadStaff")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        storageService.store(file);
        model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
        File convFile = new File(String.valueOf(storageService.load(file.getOriginalFilename())));
        try {
            parser = new FileParser(convFile);
            model.addAttribute("title", "Number of Students");
            return "studentNums";
        } catch (ParseException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/uploadStudents")
    public String uploadStudents(@RequestParam("file") MultipartFile file, Model model) {
        storageService.store(file);
        File convFile = new File(String.valueOf(storageService.load(file.getOriginalFilename())));
        try {
            List<Student> students =  parser.parseStudents(convFile);
            for(Student student:students)
                studentDAO.save(student);
            model.addAttribute("message", "Students file uploaded.");
            model.addAttribute("title", "Generate Solution");
            systemVariables.NUMBER_OF_STUDENTS = students.size();
            generateProjects(systemVariables.NUMBER_OF_STUDENTS);
            return "downloadProjects";
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

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    public void generateProjects(int number) {
        List<String[]> lines = UploadController.parser.lines;

        //List for making sure we don't add the same staff member twice
        List<Integer> already = new ArrayList<>();

        for(int i = 0; i < number / 2; i++) {
            //First, generate a staff member
            int lineNumber;
            do {
                lineNumber = ThreadLocalRandom.current().nextInt(1, lines.size() + 1);
            } while (already.contains(lineNumber));
            already.add(lineNumber);

            String[] line = lines.get(lineNumber-1);
            String name = line[0];
            Map<Integer, String> interestsMap = new HashMap<>();
            //Some staff members have no interests
            if(line[1] != null && line[1].length() > 0) {
                String[] interests = line[1].substring(1, line[1].length() - 1).split(",");
                for (String string : interests)
                    interestsMap.put(interestsMap.size(), string);
            }
            else interestsMap.put(interestsMap.size(), null);
            String stream;
            if(line[line.length-1].equals("Dagon Studies")) stream = "Dagon Studies";
            else stream = "Cthulhu Studies";
            StaffMember member = new StaffMember(name, interestsMap, stream);
            //Store and retrieve it in database so that an ID is generated
            staffMemberDAO.save(member);
            member = staffMemberDAO.findByName(member.getName());

            //Next, generate three projects that they "propose"
            for(int j = 0; j < 3; j++) {
                String projectTitle = RandomGenerator.generateString();
                String projectStream;
                if(member.getStream().equals("Dagon Studies"))
                    projectStream = "DS";
                    //Generate a random integer to decide if it's CS or CS+DS
                else if(ThreadLocalRandom.current().nextInt(0, 11) % 2 == 0)
                    projectStream = "CS";
                else projectStream = "CS+DS";
                Project project = new Project(projectTitle, member.getId(), projectStream);
                member.addProject_proposal(project.getId());
                projectDAO.save(project);
            }
            staffMemberDAO.save(member);
        }

        //Now write to a CSV
        try {
            UploadController.parser.writeProjects(projectDAO.findAll(), "user-files/projects.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
