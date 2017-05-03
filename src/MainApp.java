import City.*;
import GA.*;
import Utils.Graph;
import processing.core.*;

import java.io.PrintWriter;

public class MainApp extends PApplet{

    /* GA PARAMETERS */
    private static final int NUM_CITY = 20;
    private static final int numPop = 50;
    private static final float mutationRate = 0.1f;
    /* ------------- */

    private City[] myCity;
    private Population population;
    private Graph g;
    private CityManager cityManager;
    private PrintWriter output;

    public static void main(String args[]) {
        PApplet.main("MainApp", args);
    }

    public void settings(){ // ESEGUE SOLO UNA VOLTA
        size(1000, 600);
    }

    public void setup(){ // Loop once
        myCity = new City[NUM_CITY];
        g = new Graph(this);
        cityManager = new CityManager(myCity, this);
        //output = createWriter("distance.txt");
        //cityManager.setCircle();
        cityManager.setRandom();

        population = new Population(numPop, mutationRate, myCity, this );
    }

    public void draw(){
        background(190);

        population.startP(); // Prepares the new population (Calc distance and fitness)

        // Calculates various parameters
        population.calcMaxFitness();
        population.calcBestDistance();
        population.calcAverageFitness();
        population.calcCurrentBestDistance();

        /* TO SHOW CURRENT BEST POPULATION ELEMENT  */
        pushMatrix();
        translate(width/4 , height/2 - 70);
        stroke(0);
        cityManager.showCity();
        population.showBestRoute(); // Shows the best of the population according to the fitness
        popMatrix();
        /* ---------------------------------------- */

        /* TO SHOW THE BEST OF THE ENTIRE EVOLUTION */
        pushMatrix();
        translate(width - width/4 , height/2 - 70);
        g.showBest(population.getBestDistanceTraveller(), myCity);
        popMatrix();
        /* ---------------------------------------------- */

        // Display some information text

        text("Average Fitness " + population.getAverageFitness(), 10, 20);
        text("Generations " + population.getGenerations(), 10, 40);
        text("Mutation Rate " + mutationRate, 10, 60);
        text("Population " + numPop, 10, 80);
        text("Current best distance " + population.getCurrentBestDistance(), 250, 20);
        text("Best distance " + population.getBestDistance(), 600, 20);
        text("Fitness graph:", 10, 435);

        // Shows the fitness graph
        g.showGraphFitness(population);


        /* TO SAVE DATA ON A TXT FILE */
/*
        if (population.getGenerations() < 800) {
            output.println(Float.toString(population.getBestDistance()).replaceAll("\\.", "\\,"));
        } else {
            output.flush();
            output.close();
        }

        /* -------------------------- */

        // Complete the reproduction
        population.reproductionTournamentSelection_2(3);
    }


}
