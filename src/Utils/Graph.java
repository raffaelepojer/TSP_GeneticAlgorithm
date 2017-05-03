package Utils;

import City.City;
import GA.Population;
import GA.Traveller;
import processing.core.*;

import java.util.ArrayList;

public class Graph extends PApplet{
    private static float record = 0; // Is used to draw the graph
    private PApplet p;
    private ArrayList graph; //

    public Graph(PApplet p) {
        this.p = p;
        graph = new ArrayList();
    }

    /**
     * Show Chart fitness, only need to see if there are improvements
     */
    public void showGraphFitness(Population population) {
        int length = 990;
        int alt = 155; // Altezza grafico
        p.fill(51);
        graph.add(-(population.getAverageFitness()));
        //println(population.calcAverageFitness());
        p.noStroke();
        p.rect(5, 440, length, alt);
        p.pushMatrix();
        p.translate(5, p.height-5);
        p.beginShape();
        p.noFill();

        for(float i = 0; i < graph.size(); i++) {
            if(i < length) {
                float n = population.getMaxFitness();
                record = (record < n) ? n : record;
                p.stroke(220);
                p.vertex(i, (p.map((float)graph.get((int)i), 0, record, 0, alt))); // Scale 0 and record to 0 and the height of the box
            } else {
                // If arrives at the bottom erases the first element
                graph.remove(0);
            }
        }
        p.endShape();
        p.popMatrix();
    }

    public void showBest(Traveller traveller, City[] city) {
        // SHOW CITIES AND THE BEST ROUTE
        int numC = 0;
        for (int i = 0; i < city.length; i++, numC++) {
            city[i].display();
            p.text(numC, city[i].getLocation().x + 15, city[i].getLocation().y + 5);
            p.fill(0);
        }
        traveller.showRoute();
        // -----------
    }

}

