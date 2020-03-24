package com.cultofcthulhu.projectallocation.models;

import javax.persistence.*;
import java.util.Map;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String studentNo;
    private String stream;
    @ElementCollection
    private Map<Integer, Integer> preferences;

    public Student() {}
    public Student(String firstName, String lastName, String studentNo, String stream){
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNo = studentNo;
        this.stream = stream;
    }

    public int getId() {
        return id;
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

    public void addPreference(int i){
        preferences.put(preferences.size(), i);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int preference : preferences.values())
            sb.append(preference).append(",");
        return firstName + " " + lastName + "," + studentNo + "," + stream + "," + sb;
    }
}
