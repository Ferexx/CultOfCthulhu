package com.cultofcthulhu.projectallocation.models;

import com.cultofcthulhu.projectallocation.models.data.ProjectDAO;
import com.cultofcthulhu.projectallocation.models.data.StudentDAO;

import javax.persistence.*;
import java.util.*;

public class Solution implements Comparable<Solution>{

    private Map<Integer, Integer> solution;
    private Integer[] student_project_assignment_order;
    private double energy;
    private double fitness;

    public Solution(Map<Integer, Integer> map, Integer[] student_project_assignment_order)
    {
        this.student_project_assignment_order = student_project_assignment_order;
        solution = new HashMap<>(map);
    }

    public Solution(Map<Integer, Integer> map, double energy) {
        solution = new HashMap<>(map);
        this.energy = energy;
        fitness = -energy;
    }

    public Solution(StudentDAO studentDAO, ProjectDAO projectDAO) {
        generateSolution(studentDAO, projectDAO);
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setStudent_project_assignment_order(Integer[] student_project_assignment_order) {
        this.student_project_assignment_order = student_project_assignment_order;
    }

    public Integer[] getStudent_project_assignment_order() {
        return student_project_assignment_order;
    }

    public Map<Integer, Integer> getSolution() {
        return solution;
    }

    public void generateSolution(StudentDAO studentDao, ProjectDAO projectDAO) {
        Integer[] studentAssignmentOrder;
        if(student_project_assignment_order == null) {
            studentAssignmentOrder = new Integer[(int) studentDao.count()];
            for(int i = 0; i < studentAssignmentOrder.length; i++) studentAssignmentOrder[i] = i + 1;
            Collections.shuffle(Arrays.asList(studentAssignmentOrder));
            this.student_project_assignment_order = studentAssignmentOrder;
        }
        else studentAssignmentOrder = student_project_assignment_order;
        Map<Integer, Integer> solution = new HashMap<>();
        Boolean[] takenProjects = new Boolean[(int) projectDAO.count()];
        Arrays.fill(takenProjects, false);


        for(Integer integer : studentAssignmentOrder) {
            Student student = studentDao.getOne(integer);
            Map<Integer, Integer> preferences = student.getPreferences();
            for(int x = 0; x < preferences.size(); x++) {
                if(!takenProjects[preferences.get(x) - 1]) {
                    takenProjects[preferences.get(x) - 1] = true;
                    solution.put(student.getId(), preferences.get(x));
                    break;
                }
            }
            if(solution.get(student.getId()) == null) {
                for(int i = 0; i < takenProjects.length; i++) {
                    if(!takenProjects[i]) {
                        takenProjects[i] = true;
                        solution.put(student.getId(), i + 1);
                        break;
                    }
                }
            }
        }
        this.solution = solution;
    }

    public void change() {
        Random rand = new Random();

        int x = rand.nextInt(student_project_assignment_order.length);
        int y = rand.nextInt(student_project_assignment_order.length);
        Collections.swap(Arrays.asList(student_project_assignment_order),x,y);
    }

    public String printSolution(StudentDAO studentDAO, ProjectDAO projectDAO ){

        String out = "";

        for(Map.Entry<Integer, Integer> entry : solution.entrySet()) {
            out = out + studentDAO.getOne(entry.getKey()).getName() + " " + studentDAO.getOne(entry.getKey()).getGpa();
            int num = -1;
            for (Map.Entry<Integer, Integer> preference: studentDAO.getOne(entry.getKey()).getPreferences().entrySet())
            {
                if (preference.getValue().equals(entry.getValue())){
                    num = preference.getKey() + 1;
                }
            }
            out = out + "\t\t(" + num + ")\t\t" + projectDAO.getOne(entry.getValue()).getProject_title() + "\n";
        }
        out = out + fitness + "\n";
        return out;
    }

    public List<String[]> getSolutionList(StudentDAO studentDAO, ProjectDAO projectDAO ){

        List<String[]> stringList = new ArrayList<>();

        for(Map.Entry<Integer, Integer> entry : solution.entrySet()) {
            String[] out = new String[4];
            out[0] = studentDAO.getOne(entry.getKey()).getName();
            out[1] = String.valueOf(studentDAO.getOne(entry.getKey()).getGpa());
            int num = -1;
            for (Map.Entry<Integer, Integer> preference: studentDAO.getOne(entry.getKey()).getPreferences().entrySet())
            {
                if (preference.getValue().equals(entry.getValue())){
                    num = preference.getKey() + 1;
                }
            }
            out[2] = String.valueOf(num);
            out[3] = projectDAO.getOne(entry.getValue()).getProject_title();
            stringList.add(out);
        }
        return stringList;
    }

    @Override
    public int compareTo(Solution o) {
        return Double.compare(o.getFitness(), fitness);
    }
}
