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

    private int projectID;
    private String projectTitle;
    private long proposedBy = 0;
    private int studentAssigned;

    public Project() {}

    public Project(String project_title, int proposed_by) {
        this.projectTitle = project_title;
        this.proposedBy = proposed_by;
    }
    public Project(int id, String project_title, int proposed_by) {
        this.projectID = id;
        this.projectTitle = project_title;
        this.proposedBy = proposed_by;
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProject_title() {
        return projectTitle;
    }

    public void setProject_title(String project_title) {
        this.projectTitle = project_title;
    }

    public long getProposed_by() {
        return proposedBy;
    }

    public void setProposed_by(int proposed_by) {
        this.proposedBy = proposed_by;
    }

    public void setStudentAssigned(int studentAssigned) {
        this.studentAssigned = studentAssigned;
    }

    public int getStudentAssigned() {
        return studentAssigned;
    }

    //Extra methods
    @Override
    public String toString() {
        return proposedBy + "," + projectTitle;
    }

}
