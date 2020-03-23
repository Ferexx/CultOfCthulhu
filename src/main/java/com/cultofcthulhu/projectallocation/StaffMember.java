package com.cultofcthulhu.projectallocation;

import java.util.List;

public class StaffMember {
    private String name;
    private List<String> research_interests;
    private String stream;
    private List<Project> project_proposals;

    StaffMember (String name, List<String> research_interests,  String stream){
        this.name = name;
        this.research_interests = research_interests;
        this. stream = stream;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setResearch_interests(List<String> research_interests) {
        this.research_interests = research_interests;
    }

    public void addResearchInterest(String research_interest){
        research_interests.add(research_interest);
    }

    public List<String> getResearch_interests() {
        return research_interests;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStream() {
        return stream;
    }

    public void setProject_proposals(List<Project> project_proposals) {
        this.project_proposals = project_proposals;
    }

    public void addProject_proposal(Project projectProposal){
       project_proposals.add(projectProposal);
    }
}
