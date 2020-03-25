package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.RandomGenerator;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StaffMemberDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class GenerationController {

    @Autowired
    private StaffMemberDAO staffMemberDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private StudentDAO studentDAO;

    private static final int NUMBER_OF_PREFERENCES = 10;

    @RequestMapping(value = "/howManyStudents")
    public String howManyStudents(Model model) {
        model.addAttribute("title", "Number of Students");
        return "studentNums";
    }

    @PostMapping(value = "numStudents")
    public String numStudents(@RequestParam("number") Integer number) throws IOException {
        List<String[]> projects = generateProjects(number);
        generateStudent(number, projects);
        return "redirect:downloadProjects";
    }

    @RequestMapping(value = "downloadProjects")
        public String downloadProjects() {
            return "downloadProjects";
        }

    @RequestMapping(value = "downloadP")
    public ResponseEntity downloadP() {
        Path path = Paths.get("user-files/" + "projects.txt");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/txt")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @RequestMapping(value = "downloadS")
    public ResponseEntity downloadS() {
        Path path = Paths.get("user-files/" + "students.txt");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/txt")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    public void generateStudent(int number, List<String[]> projects) throws IOException {
        Random rand = new Random();

        File firstfile= new File("input-files/student_firstname_base.csv");
        List<String> firstnames = new ArrayList<>();

        File lastfile= new File("input-files/student_lastname_base.csv");
        List<String> lastnames = new ArrayList<>();

        Scanner inputStream;
        List<Student> students = new ArrayList<>();

        try{
            inputStream = new Scanner(firstfile);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                firstnames.add(line);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            inputStream = new Scanner(lastfile);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                lastnames.add(line);
            }
            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fname;
        String lname;
        int n = 0;
        String[] line = new String[4];

        for (int i = 0; i < number; i++) {
            fname = firstnames.get(rand.nextInt(200));
            line[0] = fname;
            lname = lastnames.get(rand.nextInt(200));
            line[1] = lname;
            line[2] = String.valueOf(n++);

            if (rand.nextInt(10) < 6){
                line[3] = "CS";
            } else {
                line[3] = "DS";
            }
            Student student = new Student(line[0], line[1], line[2], line[3]);
            assignProjects(student, projects);
            students.add(student);
        }
        UploadController.parser.writeStudents(students, "user-files/students.txt");
    }

    public void assignProjects(Student student, List<String[]> projects){
        Random rand = new Random();

        for (int x = 0 ; x < NUMBER_OF_PREFERENCES ; x++) {
            int val;
            do {
                val = (int) Math.round(rand.nextGaussian() * (projects.size() / 3) + (projects.size() / 2));
            }while(val <= 0 || val >= projects.size()-1);
            student.addPreference(projects.get(val));
        }
    }

    public void generateProjects(int number) {
        List<String[]> lines = UploadController.parser.lines;

        for(int i = 0; i < number / 2; i++) {
            //First, generate a staff member
            int lineNumber = ThreadLocalRandom.current().nextInt(0, lines.size() + 1);
            String[] line = lines.get(lineNumber-1);
            String name = line[0];
            String[] interests = line[1].substring(1, line[1].length()-1).split(",");
            Map<Integer, String> interestsMap = new HashMap<>();
            for(String string: interests)
                interestsMap.put(interestsMap.size(), string);
            String stream = line[3];
            StaffMember member = new StaffMember(name, interestsMap, stream);

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
