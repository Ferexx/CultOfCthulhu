package com.cultofcthulhu.projectallocation.models;

import javax.persistence.*;
import java.util.Map;

@Entity
public class StaffMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    @ElementCollection
    private Map<Integer, String> research_interests;
    private String stream;
    @ElementCollection
    private Map<Integer, Integer> project_proposals;

    public StaffMember() {}

    public StaffMember (String name, Map<Integer, String> research_interests,  String stream){
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

    public void setResearch_interests(Map<Integer, String> research_interests) {
        this.research_interests = research_interests;
    }

    public void addResearchInterest(String research_interest){
        research_interests.put(research_interests.size(), research_interest);
    }

    public Map<Integer, String> getResearch_interests() {
        return research_interests;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStream() {
        return stream;
    }

    public void setProject_proposals(Map<Integer, Integer> project_proposals) {
        this.project_proposals = project_proposals;
    }

    public void addProject_proposal(int projectProposal){
       project_proposals.put(project_proposals.size(), projectProposal);
    }
}
