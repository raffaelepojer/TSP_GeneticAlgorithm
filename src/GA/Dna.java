package GA;

import java.util.Arrays;
import java.util.Random;

public class Dna{

    public int size; // Size of the array
    public int[] genes;
    public Random r;

    public Dna(int[] genes) {
        this.genes = genes;
        size = genes.length;
        r = new Random();
    }

    public Dna(int size) {
        r = new Random();
        this.size = size;
        this.genes = new int[size];
        for (int i = 0; i < size; i++) {
            genes[i] = i;
        }

        // Create a random dna, exchanging the values in it
        for (int i = 0; i < size; i++) {
            int n = r.nextInt(size);
            int tmp = genes[i];
            int tmp2 = genes[n];
            genes[i] = tmp2;
            genes[n] = tmp;
        }
    }

    public Dna crossover(Dna geneA) { // SBAGLIATO
        int pointA, pointB;
        int a = r.nextInt(genes.length); // Crossover point a
        int b = r.nextInt(genes.length); // Crossover point b

        if(a <= b) {
            pointA = a;
            pointB = b;
        } else {
            pointB = a;
            pointA = b;
        }

        int[] child = geneA.genes;

        for (int i = pointA; i <= pointB ; i++) {
            int index = 0;
            for (int j = 0; j < child.length; j++) {
                if (child[j] == genes[i]) {
                    index = j;
                }
            }
            child[index] = child[i];
            child[i] = genes[i];
        }

        return (new Dna(child));
    }

    public Dna crossover2(Dna geneA) {

        // Check if the two dna have the same length
        if (this.genes.length == geneA.genes.length) {
            int pointA, pointB;
            int a = r.nextInt(genes.length); // Crossover point a
            int b = r.nextInt(genes.length); // Crossover point b

            if (a <= b) { // Is better have pointA smaller than pointB
                pointA = a;
                pointB = b;
            } else {
                pointB = a;
                pointA = b;
            }

            //System.out.println(pointA + " " + pointB);
            int[] child = new int[geneA.genes.length];

            for (int i = 0; i < genes.length; i++) {
                child[i] = geneA.genes[i];
            }

            // Paste the information of this.genes between pointA anf pointB
            for (int i = pointA; i <= pointB ; i++) {
                child[i] = genes[i];
            }
            // System.out.println("                 "  + Arrays.toString(child));

            for (int i = 0; i < genes.length; i++) {
                if ((i < pointA && i < pointB) || (i > pointA && i > pointB)) { // Check outside pointA and pointB
                    int num = geneA.genes[i];
                    for (int j = pointA; j <= pointB ; j++) { // Check inside pointA and pointB
                        // If the information is the same, swap with geneA
                        if (num == genes[j]){
                            num = geneA.genes[j];
                            child[i] = num;
                            j = pointA-1; // Restart, because it can be more duplicates
                        }
                    }
                }
            }

            return (new Dna(child));
        } else {
            System.out.println("Invalid crossover, different length");
            return null;
        }
    }

    public void mutate(float mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            float c = (float) Math.random();
            // Mutate only if the mutationRate is less than a random value between 0 and 1
            if (c <= mutationRate) {
                // If true, swap random values
                int n = r.nextInt(size);
                int tmp = genes[i];
                genes[i] = genes[n];
                genes[n] = tmp;
            }
        }
    }

    @Override
    public String toString() {
        return "GA.Dna{" +
                "size=" + size +
                ", genes=" + Arrays.toString(genes) +
                '}';
    }
}
