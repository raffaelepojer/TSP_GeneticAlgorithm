package GA;

import java.util.ArrayList;
import java.util.Random;
import City.City;
import processing.core.*;

public class Population extends PApplet{

    private ArrayList<Traveller> matingPool;
    private Traveller[] population;
    private int generations, popNum;
    private float mutationRate;
    private PApplet p;
    private City[] myCity;
    float max = 0;
    private Random r;
    private static Traveller bestDistanceTraveller;
    private static float bestDistance, averageFitness, maxFitness;
    private float currentBestDistance;

    public Population(int popNum, float mutationRate, City[] myCity, PApplet _p) {
        this.popNum = popNum;
        this.myCity = myCity;
        this.p = _p;
        this.generations = 0;
        this.mutationRate = mutationRate;
        this.generations = 0;
        this.bestDistance = MAX_FLOAT;
        this.averageFitness = 0;
        this.maxFitness = 0;
        this.r = new Random();
        this.matingPool = new ArrayList<>();
        this.bestDistanceTraveller = new Traveller();
        this.population = new Traveller[popNum];
        this.currentBestDistance = MAX_FLOAT;

        for (int i = 0; i < population.length; i++) {
            population[i] = new Traveller(myCity, _p); // Create a new random Traveller
        }
    }

    /**
     * Calc the fitness of the entire population
     */
    public void calcFitness(){
        for (Traveller t : population) {
            t.calcFitness();
        }
    }

    /**
     * Method of selection that use an ArrayList as a mating pool for select the candidates
     */
    private void naturalSelection() {
        matingPool.clear();
        for (int i = 0; i < population.length; i++) {
            float fitness = p.map(population[i].getMyFitness(), 0, maxFitness, 0, 1); // Map the value to 0 and maxFitness to 0 and 1

            // Put the element in the matingPool according to the fitness
            int n = (int) (fitness * 100);
            for (int j = 0; j < n; j++) {
                matingPool.add(population[i]);
            }
        }
        //System.out.println(matingPool.size());
    }

    /**
     * The classic roulette selection in GA
     * @return index of the element selected
     */
    private int rouletteSelection() {
        float fitness_sum = 0;
        for (Traveller t : population) {
            fitness_sum += t.getMyFitness();
        }
        float rand = r.nextFloat()*fitness_sum;
        float partialSum = 0;
        for (int i = 0; i < population.length; i++) {
            partialSum += population[i].getMyFitness();
            if (partialSum >= rand)
                return i;
        }
        return -1;
    }

    /**
     * The tournament selection, consists of selecting n elements and get the best of these according to the fitness value
     * @param n The number of element selected randomly
     * @return The best element selected
     */
    private Traveller tournamentSelection(int n) {
        Traveller[] tr = new Traveller[n]; // The list of elements selected
        float maxFit = 0;
        int index = 0;
        for (int i = 0; i < n; i++) {
            tr[i] = population[r.nextInt(population.length)]; // Get a random element
            if (maxFit < tr[i].getMyFitness()){
                maxFit = tr[i].getMyFitness();
                index = i;
            }
        }
        return tr[index];
    }

    /**
     * Select only the best
     */
    private int[] bestSelection() {
        float bestFitness = 0;
        int[] index = new int[2];
        for (int i = 0; i < population.length; i++) {
            if (bestFitness < population[i].getMyFitness()) {
                bestFitness = population[i].getMyFitness();
                index[0] = i;
            }
        }
        bestFitness = 0;
        for (int i = 0; i < population.length; i++) {
            if (bestFitness < population[i].getMyFitness() && i != index[0]) {
                bestFitness = population[i].getMyFitness();
                index[1] = i;
            }
        }
        return index;
    }

    /**
     * Generates using the nauturalSelection
     */
    private void generate() {
        for (int i = 0; i < population.length; i++) {
            // Pick two random element from the matingPool
            int a = (int)p.random(matingPool.size() - 1);
            int b = (int)p.random(matingPool.size() - 1);
            Traveller partnerA = matingPool.get(a);
            Traveller partnerB = matingPool.get(b);
            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Generates using the nauturalSelection doing the crossover with the best element
     */
    private void generate_2() {
        for (int i = 0; i < population.length; i++) {
            // Pick two random element from the matingPool
            int a = (int)p.random(matingPool.size() - 1);
            Traveller partnerA = matingPool.get(a);
            Traveller partnerB = bestDistanceTraveller;
            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Generates using rouletteSelection
     */
    private void generate2() {
        for (int i = 0; i < population.length; i++) {
            // Get the index of the two element selected
            int indexA = rouletteSelection();
            int indexB = rouletteSelection();

            Traveller partnerA = new Traveller();
            Traveller partnerB = new Traveller();

            if (indexA != -1 || indexB != -1) {
                partnerA = population[indexA];
                partnerB = population[indexB];
            } else
                System.out.println("Error in wheelSelection! Index = -1");

            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Generates using rouletteSelection doing the crossover with the best element
     */
    private void generate2_2() {
        for (int i = 0; i < population.length; i++) {
            // Get the index of the two element selected
            int indexA = rouletteSelection();

            Traveller partnerA = new Traveller();
            Traveller partnerB = new Traveller();

            if (indexA != -1) {
                partnerA = population[indexA];
                partnerB = bestDistanceTraveller;
            } else
                System.out.println("Error in wheelSelection! Index = -1");

            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Generates using bestSelection
     */
    private void generate3() {
        for (int i = 0; i < population.length; i++) {
            int[] vet = bestSelection();
            int a = vet[0];
            int b = vet[1];

            Traveller partnerA = population[a];
            Traveller partnerB = population[b];

            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);

            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Use tournamentSelection
     */
    private void generate4(int n) {
        for (int i = 0; i < population.length; i++) {
            Traveller partnerA = tournamentSelection(n);
            Traveller partnerB = tournamentSelection(n);
            Dna genesA = partnerA.getDna();
            Dna genesB = partnerB.getDna();
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Generates the element doing the crossover with the best element
     * @param n
     */
    private void generate4_2(int n) {
        for (int i = 0; i < population.length; i++) {
            Traveller partnerA = tournamentSelection(n);
            Dna genesA = partnerA.getDna();
            Dna genesB = bestDistanceTraveller.getDna(); // Crossover with the best element according to distance
            Dna child = genesA.crossover2(genesB);
            child.mutate(mutationRate);
            population[i] = new Traveller(child, myCity, p);
        }
        generations++;
    }

    /**
     * Initialises the population
     */
    public void startP() {
        for (Traveller traveller : population) {
            traveller.calcDistance();
        }
        this.calcFitness();
    }

    public void reproductionNaturalSelection(){
        naturalSelection();
        generate();
    }

    public void reproductionNaturalSelection_2(){
        naturalSelection();
        generate_2();
    }

    public void reproductionRouletteSeleciton() {
        generate2();
    }

    public void reproductionRouletteSeleciton_2() {
        generate2_2();
    }

    public void reproductionBestSelection() {
        naturalSelection();
        generate3();
    }

    public void reproductionTournamentSelection(int n) {
        generate4(n);
    }

    public void reproductionTournamentSelection_2(int n) {
        generate4_2(n);
    }

    /**
     * Retrun the best traveller according to the fitness
     * @return Best traveller
     */
    private Traveller getBest() {
        float max = 0;
        int index = 0;
        for (int i = 0; i < population.length; i++) {
            if (population[i].getMyFitness() > max) {
                max = population[i].getMyFitness();
                index = i;
            }
        }
        return population[index];
    }

    /**
     * Show the best route according to the fitness
     */
    public void showBestRoute() {
        getBest().showRoute();
    }

    /**
     * Calc average fitness
     */
    public void calcAverageFitness() {
        float total = 0;
        for (Traveller t : population) {
            total += t.getMyFitness();
        }
        averageFitness = total / (population.length);
    }

    public void calcBestDistance() {
        for (Traveller t : population) {
            if(t.getDistance() < bestDistance) {
                bestDistance = t.getDistance();
                bestDistanceTraveller = t;
            }
        }
    }

    public void calcCurrentBestDistance() {
        currentBestDistance = MAX_FLOAT;
        for (Traveller t : population) {
            if(t.getDistance() < currentBestDistance) {
                currentBestDistance = t.getDistance();
            }
        }
    }

    public void calcBestDistanceTraveller() {
        float max = MAX_FLOAT;
        for (Traveller t : population) {
            if(t.getDistance() < max) {
                max = t.getDistance();
                bestDistanceTraveller = t;
            }
        }
    }

    public void calcMaxFitness() {
        for (Traveller t : population) {
            if(t.getMyFitness() > maxFitness){
                maxFitness = t.getMyFitness();
            }
        }
    }

    public int getGenerations() {
        return generations;
    }

    public Traveller getBestDistanceTraveller() {
        return bestDistanceTraveller;
    }

    public float getBestDistance() {
        return bestDistance;
    }

    public float getAverageFitness() {
        return averageFitness;
    }

    public float getMaxFitness() {
        return maxFitness;
    }

    public float getCurrentBestDistance() {
        return currentBestDistance;
    }
}
