package com.cultofcthulhu.projectallocation.controllers;

import com.cultofcthulhu.projectallocation.CSVParser;
import com.cultofcthulhu.projectallocation.RandomGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class GenerationController {

    @RequestMapping(value = "/howManyStudents")
    public String howManyStudents(Model model) {
        model.addAttribute("title", "Number of Students");
        return "studentNums";
    }

    @PostMapping(value = "numStudents")
    public void numStudents(@RequestParam("number") Integer number) throws IOException {
        generateProjects(number);
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
