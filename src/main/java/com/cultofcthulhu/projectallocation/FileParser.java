package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public List<String[]> lines = new ArrayList<>();
    private File toParse;

    //Constructor does the actual parsing
    public FileParser(File toParse) throws ParseException {
        this.toParse = toParse;
        String split;
        //Set splitter based on if it's csv or tsv. If it's neither, throw an error
        if (toParse.getName().substring(toParse.getName().length()-3).equals("csv"))
            split=",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";      //Regex to ignore ", basically checks ahead for how many ", and splits on the comma if that comma has zero or even number of quotes ahead of it
        else if(toParse.getName().substring(toParse.getName().length()-3).equals("csv")) split="\t";
        else throw new ParseException("Please make sure your file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        parse(split);
    }

    public void parse(String split) throws ParseException{
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(toParse));
            int i = 1;
            while((line = br.readLine())!=null) {
                String[] values = line.split(split, -1);
                if(values.length!=4) throw new ParseException("Your file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: 4)");
                lines.add(values);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //File parser doubles as a file writer :)
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

