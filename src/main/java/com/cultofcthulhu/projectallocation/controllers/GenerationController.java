package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.CSVParser;
import com.cultofcthulhu.projectallocation.RandomGenerator;
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
import org.w3c.dom.Text;

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

    @RequestMapping(value = "/howManyStudents")
    public String howManyStudents(Model model) {
        model.addAttribute("title", "Number of Students");
        return "studentNums";
    }

    @PostMapping(value = "numStudents")
    public String numStudents(@RequestParam("number") Integer number) throws IOException {
        generateProjects(number);
        return "redirect:downloadProjects";
    }

    @RequestMapping(value = "downloadProjects")
        public String downloadProjects() {
            return "downloadProjects";
        }

    @RequestMapping(value = "download")
    public ResponseEntity download() {
        Path path = Paths.get("files/" + "projects.txt");
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/txt")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    public List<String[]> generateStudent(int number) throws IOException {
        Random rand = new Random();

        String firstName_file= "student_firstname_base.csv";
        File firstfile= new File(firstName_file);
        List<String> firstnames = new ArrayList<>();

        String lastName_file= "student_lastname_base.csv";
        File lastfile= new File(lastName_file);
        List<String> lastnames = new ArrayList<>();

        Scanner inputStream;
        List<String[]> students = new ArrayList<>();

        try{
            inputStream = new Scanner(firstfile);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                firstnames.add(line);
            }


            inputStream.close();
        }catch (FileNotFoundException e) { }

        try{
            inputStream = new Scanner(lastfile);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                lastnames.add(line);
            }
            inputStream.close();
        }catch (FileNotFoundException e) { }

        String fname;
        String lname;
        int n = 0;
        String Str;
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

            students.add(line);
        }
        return students;
    }

    public void generateProjects(int number) throws IOException {
        List<String[]> lines = UploadController.parser.lines;
        List<String[]> newLines = new ArrayList<>();
        for(int i=0;i<number;i++) {
            int lineNumber = ThreadLocalRandom.current().nextInt(0, lines.size() + 1);
            String[] line = lines.get(lineNumber-1);
            for(int j=0;j<3;j++) {
                String[] newLine = new String[3];
                newLine[0] = line[0];
                newLine[1] = RandomGenerator.generateString();
                if(line[line.length-1].equals("Dagon Studies") && ThreadLocalRandom.current().nextInt(0, 11) % 2 == 0) {
                    newLine[2] = "CS+DS";
                }
                else if (line[line.length-1].equals("Dagon Studies") && ThreadLocalRandom.current().nextInt(0, 11) % 2 == 1) {
                    newLine[2] = "CS";
                }
                else newLine[2] = "CS";
                newLines.add(newLine);

                if(line[line.length-1].equals("Dagon Studies")) {
                    newLine[2] = "DS";
                }
                else if(ThreadLocalRandom.current().nextInt(0, 11) % 2 == 0) {
                    newLine[2] = "CS";
                }
                else newLine[2] = "CS+DS";
            }
        }
        UploadController.parser.writeCSV(newLines);
    }
}
