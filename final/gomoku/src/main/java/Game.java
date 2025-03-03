package org.openjfx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Game {
    // default Point generate_random_move(Map map){

    // } 
    // default ArrayList<Point> generate_random_candidat(Map map){

    // }
    // default ArrayList<float> generate_random_candidats_stats(int nbr){
    //     //float[] val, ArrayList<Candidat.coord> lst
    // }
    private static MinMax minmax;
    private ArrayList<Point> candidats;
    private ArrayList<Float> stats;

    public ArrayList<Point> getCandidat(){
        return candidats;
    }

    public ArrayList<Float> getStats(){
        return stats;
    }

    private ArrayList<Point> generateRandomCandidats(int n) {
        Random rand = new Random();
        ArrayList<Point> lst = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(19);
            int y = rand.nextInt(19);
            lst.add(new Point(x, y));
        }
        
        return lst; 
    }

    private ArrayList<Float> generate_random_stats(int n) {
        ArrayList<Float> randomValues = new ArrayList<>();
        Random random = new Random();
        // Génération des nombres aléatoires
        for (int i = 0; i < n; i++) {
            randomValues.add(random.nextFloat()); // Génère un float entre 0.0 et 1.0
        }
        // Tri des valeurs en ordre croissant
        Collections.sort(randomValues);
        return randomValues;
    }

    public Point do_ia_move(){
        candidats = generateRandomCandidats(6);
        stats = generate_random_stats(6);
        return candidats.get(0);
    }
}
