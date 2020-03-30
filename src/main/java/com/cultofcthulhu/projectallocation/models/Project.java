package com.cultofcthulhu.projectallocation.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project {

    //Variable declaration and constructors
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String project_title;
    private int proposed_by;
    private String suitable_for_streams;
    private int studentAssigned;

    public Project() {}

    public Project(String project_title, int proposed_by, String suitable_for_streams){
        this.project_title = project_title;
        this.proposed_by = proposed_by;
        this.suitable_for_streams = suitable_for_streams;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public int getProposed_by() {
        return proposed_by;
    }

    public void setProposed_by(int proposed_by) {
        this.proposed_by = proposed_by;
    }

    public String getSuitable_for_streams() {
        return suitable_for_streams;
    }

    public void setSuitable_for_streams(String suitable_for_streams) {
        this.suitable_for_streams = suitable_for_streams;
    }

    //Extra methods
    @Override
    public String toString() {
        return proposed_by + "," + project_title + "," + suitable_for_streams;
    }

}
