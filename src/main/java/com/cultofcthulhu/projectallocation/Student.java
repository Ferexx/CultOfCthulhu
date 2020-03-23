package com.cultofcthulhu.projectallocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    private String firstName;
    private String lastName;
    private String studentNo;
    private String stream;
    private List<String[]> preferences = new ArrayList<>();

    public Student(String firstName, String lastName, String studentNo, String stream){
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNo = studentNo;
        this.stream = stream;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getStream() {
        return stream;
    }

    public void addPreference(String[] i){
        preferences.add(i);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String[] preference : preferences)
            sb.append(preference[1]).append(",");
        return firstName + " " + lastName + "," + studentNo + "," + stream + "," + sb;
    }
}
