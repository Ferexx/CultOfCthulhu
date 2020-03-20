package com.cultofcthulhu.projectallocation;

import java.util.List;

public class Student {
    String firstname;
    String lastname;
    String studentNo;
    String stream;
    List<Integer> projects;

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

    public void addProject(Integer i){
        projects.add(i);
    }

    @Override
    public String toString(){
        return firstname + "," + lastname + "," + studentNo + "," + stream;
    }
}
