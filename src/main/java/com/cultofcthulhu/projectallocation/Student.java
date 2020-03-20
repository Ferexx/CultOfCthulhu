package com.cultofcthulhu.projectallocation;

public class Student {
    String firstname;
    String lastname;
    String studentNo;
    String stream;

    public Student(String firstname, String lastname, String studentNo, String stream){
        this.firstname = firstname;
        this.lastname = lastname;
        this.studentNo = studentNo;
        this.stream = stream;
    }

    @Override
    public String toString(){
        return firstname + " " + lastname + " " + studentNo + " " + stream;
    }
}
