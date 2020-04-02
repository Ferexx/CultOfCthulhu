package com.cultofcthulhu.projectallocation.models;

import com.cultofcthulhu.projectallocation.interfaces.Personable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Student implements Personable {

    //Variable declaration and constructors
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String stream;
    @ElementCollection
    private Map<Integer, Integer> preferences = new HashMap<>();
    private int assignedProjectID = -1;
    private int workingUnderStaffID;

    public Student() {}
    public Student(String firstName, String lastName, String stream){
        this.firstName = firstName;
        this.lastName = lastName;
        this.stream = stream;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() { return firstName + " " + lastName; }

    public void setName(String name) {
        firstName = name.substring(0,name.indexOf(" "));
        lastName = name.substring(name.indexOf(" ") + 1);
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) { this.stream = stream; }

    public void addPreference(int i){
        preferences.put(preferences.size(), i);
    }

    public void setAssignedProjectID(int i){
        assignedProjectID = i;
    }

    public int getAssignedProjectID() {
        return assignedProjectID;
    }

    public void setWorkingUnderStaffID(int i){
        workingUnderStaffID = i;
    }

    public int getWorkingUnderStaffID() {
        return workingUnderStaffID;
    }

    public Map<Integer, Integer> getPreferences() {
        return preferences;
    }

    //Extra methods
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int preference : preferences.values())
            sb.append(preference).append(",");
        return firstName + " " + lastName + "," + id + "," + stream + ",\"" + sb + "\"";
    }
}
