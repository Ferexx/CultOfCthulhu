package com.cultofcthulhu.projectallocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    String firstname;
    String lastname;
    String studentNo;
    String stream;
    List<String[]> projects = new ArrayList<>();

    public Student(String firstname, String lastname, String studentNo, String stream){
        this.firstname = firstname;
        this.lastname = lastname;
        this.studentNo = studentNo;
        this.stream = stream;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getStream() {
        return stream;
    }

    public void addProject(String[] i){
        projects.add(i);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String[] project : projects)
            sb.append(project[1]).append(",");
        return firstname + " " + lastname + "," + studentNo + "," + stream + "," + sb;
    }
}
