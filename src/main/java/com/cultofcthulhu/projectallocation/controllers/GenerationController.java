package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.RandomGenerator;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StaffMemberDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.solvers.SolutionByLottery;
import com.cultofcthulhu.projectallocation.system.systemVariables;
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
import java.text.DecimalFormat;
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
    public String numStudents(@RequestParam("number") Integer number) {
        generateProjects(number);
        generateStudents(number, projectDAO.findAll());
        systemVariables.NUMBER_OF_STUDENTS = number;
        return "redirect:downloadProjects";
    }

    @RequestMapping(value = "downloadProjects")
        public String downloadProjects() {
            return "downloadProjects";
        }

    @RequestMapping(value = "downloadP")
    public ResponseEntity downloadP() {
        Path path = Paths.get("user-files/" + "projects.csv");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/csv")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @RequestMapping(value = "downloadS")
    public ResponseEntity downloadS() {
        Path path = Paths.get("user-files/" + "students.csv");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/csv")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    public void generateStudents(int number, List<Project> projects) {
        Random rand = new Random();
        File firstNameFile = new File("input-files/student_firstname_base.csv");
        List<String> firstnames = new ArrayList<>();
        File lastNameFile = new File("input-files/student_lastname_base.csv");
        List<String> lastnames = new ArrayList<>();
        Scanner inputStream;

        //Read first and last names into their respective lists
        try {
            inputStream = new Scanner(firstNameFile);
            while(inputStream.hasNext()) {
                String line = inputStream.next();
                firstnames.add(line);
            }
            inputStream.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            inputStream = new Scanner(lastNameFile);
            while(inputStream.hasNext()) {
                String line = inputStream.next();
                lastnames.add(line);
            }
            inputStream.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        //Now randomly choose first and last name, and generate student
        String firstname, lastname, stream;
        for(int i = 0; i < number; i++) {
            firstname = firstnames.get(rand.nextInt(200));
            lastname = lastnames.get(rand.nextInt(200));
            if(rand.nextInt(10) < 6) stream = "CS";
            else stream = "DS";
            Student student = new Student(firstname, lastname, stream);
            DecimalFormat df = new DecimalFormat("#.#");
            student.setGpa(Double.parseDouble(df.format(ThreadLocalRandom.current().nextDouble(0, 4.2))));
            //Generate this student's preferences with Gaussian distribution
            assignPreferences(student, projects);
        }

        //Finally, write to CSV
        try {
            UploadController.parser.writeStudents(studentDAO.findAll(), "user-files/students.csv");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void assignPreferences(Student student, List<Project> projects) {
        Random rand = new Random();

        for (int x = 0 ; x < NUMBER_OF_PREFERENCES ; x++) {
            int val;
            do {
                val = (int) Math.round(rand.nextGaussian() * (projects.size() / 3) + (projects.size() / 2));
            }while(val <= 0 || val >= projects.size()-1);
            student.addPreference(projects.get(val).getId());
        }
        studentDAO.save(student);
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
