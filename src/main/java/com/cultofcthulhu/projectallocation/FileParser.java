package com.cultofcthulhu.projectallocation;

import com.cultofcthulhu.projectallocation.exceptions.ParseException;
import com.cultofcthulhu.projectallocation.models.Project;
import com.cultofcthulhu.projectallocation.models.StaffMember;
import com.cultofcthulhu.projectallocation.models.Student;
import com.cultofcthulhu.projectallocation.models.StudentProject;
import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentProjectDAO;
import com.cultofcthulhu.projectallocation.system.systemVariables;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileParser {

    public FileParser() {}

    public void parseMainFile(File file, StudentDAO studentDAO, ProjectDAO projectDAO, StudentProjectDAO studentProjectDAO) throws ParseException, IOException {
        String split;
        if(file.getName().endsWith("csv")) split = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else if(file.getName().endsWith("tsv")) split = "\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else throw new ParseException(
                "Please make sure your file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int i = 1;
        while((line = br.readLine()) != null) {
            String[] values = line.split(split, -1);
            //Ignore column titles
            if(values[0].contains("Student")) {
                i++;
                continue;
            }
            if(values.length < 5) throw new ParseException(
                    "Your file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: >4)");
            try {
                if(values[3].contains("student")) {
                    studentProjectDAO.save(new StudentProject(values[0], Integer.parseInt(values[1]), Double.parseDouble(values[2]), values[4]));
                    continue;
                }
                else {
                    Student student = new Student(values[0], Integer.parseInt(values[1]), Double.parseDouble(values[2]));
                    for (int j = 4; j < values.length; j++) {
                        values[j] = values[j].trim();
                        Optional<Project> project = projectDAO.findByProjectTitle(values[j]);
                        if (!project.isPresent() && !values[j].equals("")) {
                            projectDAO.save(new Project(values[j], 0));
                            student.addPreference(projectDAO.findByProjectTitle(values[j]).get().getId());
                        }
                        else if(!values[j].equals("")) {
                            student.addPreference(projectDAO.findByProjectTitle(values[j]).get().getId());
                        }
                    }
                    studentDAO.save(student);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new ParseException("Please ensure the numbers in row " + i + " are formatted correctly");
            }
            i++;
        }
        systemVariables.NUMBER_OF_STUDENTS = i;
    }

    public List<StaffMember> parseStaff(File file) throws ParseException, IOException, NumberFormatException{
        String split;
        List<StaffMember> staff = new ArrayList<>();
        if(file.getName().endsWith("csv")) split = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else if(file.getName().endsWith("tsv")) split = "\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else throw new ParseException(
                    "Please make sure your staff members file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int i = 1;
        while((line = br.readLine())!=null) {
            String[] values = line.split(split, -1);
            //Ignore column titles
            if(values[0].equals("ID")) {
                i++;
                continue;
            }
            if(values.length != 5) throw new ParseException(
                    "Your staff members file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: 5)");
            staff.add(new StaffMember(Integer.parseInt(values[0]), values[1], values[2]));
            i++;
        }
        return staff;
    }

    public List<Student> parseStudents(File file) throws ParseException, IOException, NumberFormatException {
        String split;
        List<Student> students = new ArrayList<>();
        if(file.getName().endsWith("csv")) split = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else if(file.getName().endsWith("tsv")) split = "\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else throw new ParseException(
                "Please make sure your students file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int i = 1;
        while((line = br.readLine())!=null) {
            String[] values = line.split(split, -1);
            //Ignore column titles
            if(values[0].equals("ID")) {
                i++;
                continue;
            }
            if(values.length != 5) throw new ParseException(
                    "Your students file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: 5)");
            values[3] = values[3].substring(1, values[3].length()-1);
            String[] preferences = values[3].split(split);
            if(preferences.length != systemVariables.NUMBER_OF_PREFERENCES) throw new ParseException(
                    "The student on line " + i + " in your students file does not have the correct number of preferences. (Found: " + preferences.length + ", Expected: " + systemVariables.NUMBER_OF_PREFERENCES + ")");
            /*Student student = new Student(values[0], values[1], values[2], Double.parseDouble(values[4]));
            for(String string : preferences)
                student.addPreference(Integer.parseInt(string));
            students.add(student);
            i++;*/
        }
        return students;
    }

    public List<Project> parseProjects(File file) throws ParseException, IOException {
        String split;
        List<Project> projects = new ArrayList<>();
        if(file.getName().endsWith("csv")) split = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else if(file.getName().endsWith("tsv")) split = "\t(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        else throw new ParseException(
                    "Please make sure your projects file is in .csv or .tsv format (values separated by commas or tabs, respectively), and saved as such.");
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        int i = 1;
        while((line = br.readLine()) != null) {
            String[] values = line.split(split, -1);
            //Ignore column titles
            if(values[0].equals("ID")) {
                i++;
                continue;
            }
            if(values.length != 4) throw new ParseException(
                    "Your projects file has an incorrect number of fields on line " + i + ". (Found: " + values.length + ", Expected: 4)");
            //projects.add(new Project(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), values[3]));
            i++;
        }
        return projects;
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
        writer.write("Staff Member ID,Project Title");
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
        writer.write("Student Name,Student Number,Project Preferences,GPA");
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

