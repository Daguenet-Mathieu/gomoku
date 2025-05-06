package org.interfacegui;
import org.utils.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalTime;
import java.time.LocalDate;
import java.io.File;


public class SGF{

    private static ArrayList<Map> game_moves;
    private static String rules;
    private static File file;

    private static final String[] ignore = new String[] {
        "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AB", "AW",
        "AR", "CR", "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
        "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
        "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
        "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
        "TW", "AS", "IP", "IY", "SE", "SU", "RU"
    };

    private static final String[] supported = new String[] {"KM", "HA", "AP", "CA", "FF", "GM", "SZ", "C", "AE", "PL", "B", "W", "BL", "WL"};
// "KM"//komi
// "HA"//handicap??
// *AP  Application     root	      composed simpletext ':' number // je garde
// *CA  Charset         root	      simpletext//je garde 
// FF   Fileformat      root	      number (range: 1-4)//je garde
// GM   Game            root	      number (range: 1-5,7-17)// je garde

// !SZ  Size            root	      (number | composed number ':' number)//je garde
// C    Comment         -                text  //je garde
// AE   Add Empty       setup            list of point  //je garde
// PL   Player to play  setup            color  //je garde
// B    Black           move             move //je garde
// W    White           move             move //je garde
// BL   Black time left move             real //on verra? utile juste en mode observation
// WL   White time left move             real//on verra? utile juste en mode observation'


    // private static ArrayList<Map> build_sgf(){
    //     return null;
    // }

    public static File openSGFDir(){
        try {
            File directory = new File("sgf");
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Le dossier 'sgf' a été créé.");
                }
                else {
                    System.out.println("Impossible de créer le dossier 'sgf'.");
                    return null;
                }
            }
            return directory;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;

    }

    private static String add_moves(String fileContent, ArrayList<Map> map){
            final String alpha = "abcdefghijklmnopqrstuvwxyz";
            final String[] move = new String[] { "B", "W" };
            int i = 0;
            for (Map m : map){
                if (m.getLastMove() != null){
                    fileContent += ";" + move[i];
                    fileContent += "[" + alpha.charAt(m.getLastMove().x) + "" + alpha.charAt(m.getLastMove().y)  + "]";
                    if (m.get_prisonners() != null && m.get_prisonners().size() > 0)
                        fileContent += "\n;AE";
                    for (Point p : m.get_prisonners()){
                        fileContent += "[" + alpha.charAt(p.x) + "" + alpha.charAt(p.y) + "]";
                    }
                    fileContent += "\n";
                    i^=1;
                }
            }
            fileContent = fileContent.substring(0, fileContent.length() - 1);
            fileContent += ")";
            return fileContent;
    }

    public static void createSgf(ArrayList<Map> map, String rule){
        //creer le filename
            final int rule_type = "go".equals(rule) ? 1 : 4;
            String fileContent = "(;FF[4] " + "GM[" + rule_type + "4] SZ[19] CA[UTF-8] AP[CGoban:3]\n";
            LocalDate localDate = LocalDate.now();
            LocalTime localTime = LocalTime.now();
            String fileName = localDate.toString() + "_" + localTime.toString();
            if (fileName.indexOf(".") != -1)
                fileName = "./sgf/" + rule + "_" + fileName.substring(0, fileName.indexOf("."));
            File file = new File(fileName + ".sgf");
            int i = 1;
            while (file.exists()){
                file = new File(fileName + "(" + i + ")" + ".sgf");
                i++;
            }
            file.setWritable(true);
            file.setReadable(true);
            // write(new Path(file.getPath()), ff.getBytes());
            try {
                Writer writer = new FileWriter(file, true);
                fileContent = add_moves(fileContent, map);
                writer.write(fileContent, 0, fileContent.length());
                writer.close();
            }
            catch(Exception e){
                System.out.println(e);
            }

        //ouvrir le dossier sgf si succes remplir le fichier //sinon message d'erreur?
    }

    private static String getExtension(File file){
        if (file != null){
            String filename = file.getName();
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex >= 0) {
                return filename.substring(dotIndex + 1);
            }
        }
        return null;
    }

    public static boolean parseFile(){
        if ("sgf".equals(getExtension(file)) == false)
            return false;
        //fichier demarre par ( et termine par )
        //chaque coup demarre par un ;
        //get end content go to next ] et return index a utliser dans get value et dans skip val
        //si non gere skip val en boucle
        //si gere add a la map comme un coup normal si coup invalide rejeter le fichier
        //si ( branche ls 1er c'est la 1er et la suite de l'actuelle un fois ) sauter tout les () et ski aussi les () dans () "(((...)))"si brancheS s'attendre a )) a la fin
        return true;
    }

    public static String get_game_rule(){
        return rules;
    }

    public static ArrayList<Map> get_game_moves(){
        return game_moves;
    }

    public static void setFile(String filename){
        file = new File(filename);
    }
}
