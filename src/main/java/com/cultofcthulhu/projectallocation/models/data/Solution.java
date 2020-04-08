package com.cultofcthulhu.projectallocation.models.data;

import javax.persistence.*;
import java.util.Map;
import java.util.Random;

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

    public Map<Integer, Integer> change(int degree) {
        Random rand = new Random();
        for(int i = 0; i < degree; i++) {
            int randInt;
            int randInt2;
            do {
                randInt = rand.nextInt(solution.size() - 1);
                randInt2 = rand.nextInt(solution.size() - 1);
            }while (randInt != randInt2);
            int temp = solution.get(randInt);
            int temp2 = solution.get(randInt2);
            solution.replace(randInt, temp2);
            solution.replace(randInt2, temp);
        }
        return solution;
    }
}
