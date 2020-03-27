package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public List<String[]> lines = new ArrayList<>();

    public CSVParser(File toParse) {
        String line = "";
        String split;
        if (toParse.getName().substring(toParse.getName().length()-3, toParse.getName().length()).equals("csv"))
            split=",";
        else split="\t";
        try {
            BufferedReader br = new BufferedReader(new FileReader(toParse));
            while((line = br.readLine())!=null) {
                String[] values = line.split(split);
                lines.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void writeProjects(List<Project> projects, String file) throws IOException {
        File writeTo = new File(file);
        if(writeTo.createNewFile()) {
            System.out.println("Created file");
        }
        else {
            System.out.println("File already exists");
        }

        FileWriter writer = new FileWriter(file);
        writer.write("Staff Member ID,Project Title,Stream");
        writer.write("\n");
        for(Project project : projects) {
            String string = project.toString();
            writer.write(string);
            writer.write("\n");
        }
        writer.close();
        System.out.println("Wrote to file");
    }

    public void writeStudents(List<Student> lines, String file) throws IOException {
        File writeTo = new File(file);
        if(writeTo.createNewFile()) {
            System.out.println("Created file");
        }
        else {
            System.out.println("File already exists");
        }

        FileWriter writer = new FileWriter(file);
        writer.write("Student Name,Student Number,Stream,Project Preferences");
        writer.write("\n");
        for(Student student : lines) {
            String string = student.toString();
            writer.write(string);
            writer.write("\n");
        }
        writer.close();
        System.out.println("Wrote to file");
    }
}

