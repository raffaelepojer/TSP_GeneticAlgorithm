package City;

import org.omg.CORBA.PUBLIC_MEMBER;
import processing.core.*;

public class CityManager extends PApplet{

    private PApplet p;
    private City[] city;

    public CityManager(City[] c, PApplet p) {
        this.p = p;
        this.city = c;
    }

    public void setCircle() {
        float angle = 0;
        float angle_step = TWO_PI / city.length;
        // SETTING THE CITY
        angle = 0;
        float current_radius = 175;
        for (int i = 0; i < city.length; i++) {
            City current_city = new City(p, cos(angle) * current_radius, sin(angle) * current_radius); // It puts the city at the center
            current_city.setRadius(10);
            city[i] = current_city;
            angle += angle_step;
        }
        // ----------------
    }

    public void setRandom() {
        for (int i = 0; i < city.length; i++) {
            City current_city = new City(p, random(-200, 200), random(-200, 200));
            current_city.setRadius(10);
            city[i] = current_city;
        }
    }

    public void showCity() {
        int numC = 0;
        for (int i = 0; i < city.length; i++, numC++) {
            this.city[i].display();
            p.text(numC, city[i].location.x + 15, city[i].location.y + 5);
            p.fill(0);
        }
    }
}
