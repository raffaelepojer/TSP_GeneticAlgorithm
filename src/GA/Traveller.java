package GA;

import City.City;
import processing.core.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Traveller extends PApplet {

    private float distance, myFitness, arrowDim;
    private PVector location;
    private Dna dna;
    private ArrayList<Float> localDistance;
    private City[] myCity;
    private PApplet p;

    public Traveller() {
        //
    }

    public Traveller(Dna dna, City[] myCity, PApplet p) {
        this.dna = dna;
        localDistance = new ArrayList<>();
        this.myCity = myCity;
        this.p = p;
        this.myFitness = 0;
        calcFitness();
        arrowDim = 10;
        distance = 0;
        calcDistance();
    }

    public Traveller(City[] myCity, PApplet p) {
        this.myCity = myCity;
        localDistance = new ArrayList<>();
        this.p = p;
        dna = new Dna(myCity.length);
        arrowDim = 10;
        this.myFitness = 0;
        calcFitness();
        distance = 0;
        calcDistance();
    }

    /**
     * Calculates the distance and saves localDistance
     */
    public void calcDistance() {
        distance = 0;
        float dist = 0;
        // Calculates the distance of each city
        for (int i = 0; i < myCity.length - 1; i++) {
            dist = myCity[dna.genes[i]].getDistanceTo(myCity[dna.genes[i + 1]]);
            distance += dist;
            localDistance.add(dist);
        }
        // Last-first distance
        dist = myCity[dna.genes[dna.genes.length-1]].getDistanceTo(myCity[dna.genes[0]]);
        distance += dist;
        localDistance.add(dist);
    }

    /**
     * Calculates fitness
     */
    public void calcFitness(){
        myFitness = (float) Math.pow((1 / distance), 3); // Exponential function
    }

    /**
     * Shows the location of each travellers
     */
    public void showRoute(){
        // Draw the first red line
        p.stroke(255, 0, 0);
        p.fill(255, 0, 0);
        drawArrow(myCity[dna.genes[0]].getLocation(), myCity[dna.genes[1]].getLocation(), arrowDim);
        p.stroke(0);
        p.fill(0);
        for (int i = 1; i < dna.genes.length-1; i++) {
            drawArrow(myCity[dna.genes[i]].getLocation(), myCity[dna.genes[i+1]].getLocation(), arrowDim);
        }
        // Draw first-last
        drawArrow(myCity[dna.genes[dna.genes.length-1]].getLocation(), myCity[dna.genes[0]].getLocation(), arrowDim);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getMyFitness() {
        return myFitness;
    }

    public void setMyFitness(float myFitness) {
        this.myFitness = myFitness;
    }

    public float getArrowDim() {
        return arrowDim;
    }

    public void setArrowDim(float arrowDim) {
        this.arrowDim = arrowDim;
    }

    public PVector getLocation() {
        return location;
    }

    public void setLocation(PVector location) {
        this.location = location;
    }

    public Dna getDna() {
        return dna;
    }

    public void setDna(Dna dna) {
        this.dna = dna;
    }

    public ArrayList<Float> getLocalDistance() {
        return localDistance;
    }

    public void setLocalDistance(ArrayList<Float> localDistance) {
        this.localDistance = localDistance;
    }

    public City[] getMyCity() {
        return myCity;
    }

    public void setMyCity(City[] myCity) {
        this.myCity = myCity;
    }

    public PApplet getP() {
        return p;
    }

    public void setP(PApplet p) {
        this.p = p;
    }

    /**
     * Draw arrow with tip size
     * @param p1
     * @param p2
     * @param dimension
     */
    private void drawArrow(PVector p1, PVector p2, float dimension){
        p.line(p1.x, p1.y, p2.x, p2.y);
        p.pushMatrix();
        p.translate(p2.x, p2.y);
        float a = p.atan2(p1.x-p2.x, p2.y-p1.y);
        p.rotate(a);
        p.triangle(0, 0, -dimension/2, -dimension, dimension/2, -dimension );
        p.popMatrix();
    }

    @Override
    public String toString() {
        return "GA.Traveller{" +
                "distance=" + distance +
                ", myFitness=" + myFitness +
                ", arrowDim=" + arrowDim +
                ", location=" + location +
                ", dna=" + dna +
                ", localDistance=" + localDistance +
                ", myCity=" + Arrays.toString(myCity) +
                ", p=" + p +
                '}';
    }
}
