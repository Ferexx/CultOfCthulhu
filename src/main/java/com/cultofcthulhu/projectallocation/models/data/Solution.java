package com.cultofcthulhu.projectallocation.models.data;

import javax.persistence.*;
import java.util.Map;

@Entity
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection
    private Map<Integer, Integer> solution;
    private double energy;
    private double fitness;

    public int getId() {
        return id;
    }

    public Solution(Map<Integer, Integer> map) {
        solution = map;
    }
    public Solution(Map<Integer, Integer> map, double energy) {
        solution = map;
        this.energy = energy;
        fitness = 1/energy;
    }
    public Solution() {}

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

    public Map<Integer, Integer> getSolution() {
        return solution;
    }

    public Map<Integer, Integer> change(int key, int value) {
        solution.replace(key, value);
        return solution;
    }
}