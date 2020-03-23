package com.cultofcthulhu.projectallocation;

import java.util.List;

public class Project {
    private String project_title;
    private StaffMember proposed_by;
    private List<String> suitable_for_streams;

    Project(String project_title, StaffMember proposed_by, List<String> suitable_for_streams){
        this.project_title = project_title;
        this.proposed_by = proposed_by;
        this.suitable_for_streams = suitable_for_streams;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public StaffMember getProposed_by() {
        return proposed_by;
    }

    public void setProposed_by(StaffMember proposed_by) {
        this.proposed_by = proposed_by;
    }

    public List<String> getSuitable_for_streams() {
        return suitable_for_streams;
    }

    public void setSuitable_for_streams(List<String> suitable_for_streams) {
        this.suitable_for_streams = suitable_for_streams;
    }

    public void addSuitable_for_streams(String stream) {
        suitable_for_streams.add(stream);
    }
}
