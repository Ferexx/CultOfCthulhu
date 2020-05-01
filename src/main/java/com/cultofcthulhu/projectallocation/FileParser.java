package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.system.systemVariables;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public List<String[]> lines = new ArrayList<>();
    private File toParse;

    public FileParser() {}
    //Constructor does the actual parsing
    public FileParser(File toParse) throws ParseException {
        this.toParse = toParse;
        String split;
        //Set splitter based on if it's csv or tsv. If it's neither, throw an error
        if (toParse.getName().endsWith("csv"))
            split=",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";      //Regex to ignore ", basically checks ahead for how many ", and splits on the comma if that comma has zero or even number of quotes ahead of it
        else if(toParse.getName().endsWith("tsv")) split="\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
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

    public List<Student> parseStudents(File file) throws ParseException, IOException, NumberFormatException {
        String split;
        List<Student> students = new ArrayList<>();
        if(file.getName().endsWith("csv")) split = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else if(file.getName().endsWith("tsv")) split = "\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else throw new ParseException(
                "Please make sure your file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int i = 1;
        while((line = br.readLine())!=null) {
            String[] values = line.split(split, -1);
            if(values.length != 5) throw new ParseException(
                    "Your file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: 5)");
            values[3] = values[3].substring(1, values[3].length()-1);
            String[] preferences = values[3].split(split);
            //TODO: Change 10 to numofpreferences in sysvariables
            if(preferences.length != 10) throw new ParseException(
                    "The student on line " + i + " does not have the correct number of preferences. (Found: " + preferences.length + ", Expected: " + 10 + ")");
            Student student = new Student(values[0], values[1], values[2], Double.parseDouble(values[4]));
            for(String string : preferences)
                student.addPreference(Integer.parseInt(string));
            students.add(student);
            i++;
        }
        return students;
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
        writer.write("Student Name,Student Number,Stream,Project Preferences,GPA");
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

