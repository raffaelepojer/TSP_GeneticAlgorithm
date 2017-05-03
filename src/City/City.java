package City;

import processing.core.*;

public class City extends PApplet{

    PApplet p;
    PVector location;
    float radius;

    public City(PApplet p, float x, float y) {
        this.p = p;
        location = new PVector(x, y);
        this.radius = 10;
    }

    public float getDistanceTo(City city) {
        return this.location.dist(city.location);
    }

    public void display() {
        p.fill(75);
        p.noStroke();
        p.ellipse(location.x, location.y, radius, radius);
    }

    public void display(int color) {
        p.fill(color);
        p.ellipse(location.x, location.y, radius, radius);
    }

    public PVector getLocation() {
        return location;
    }

    public void setLocation(PVector location) {
        this.location = location;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
