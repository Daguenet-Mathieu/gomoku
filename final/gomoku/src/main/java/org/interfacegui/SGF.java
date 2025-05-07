package org.interfacegui;
import org.utils.*;
import java.util.ArrayList;
// import java.io.*;
import java.io.FileWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.LocalDate;
import java.io.File;
import java.util.LinkedHashMap;


public class SGF{

    private static File file;
    private static String rules;
    private static ArrayList<Map> game_moves;
    private static final String[] ignore = new String[] {
        "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AB", "AW",
        "AR", "CR", "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
        "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
        "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
        "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
        "TW", "AS", "IP", "IY", "SE", "SU", "FF"
    };

    private static final String[] supported = new String[] {"KM", "HA", "AP", "CA", "GM", "RU", "SZ", "C", "AE", "PL", "B", "W", "BL", "WL"};
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
            e.printStackTrace();
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
                    i^=1;//changer pour get_color de Map
                }
            }
            fileContent = fileContent.substring(0, fileContent.length() - 1);
            fileContent += ")";
            return fileContent;
    }

    public static void createSgf(ArrayList<Map> map, String rule){
        //creer le filename
            final int rule_type = "go".equals(rule) ? 1 : 4;
            String fileContent = "(;FF[4] " + "GM[" + rule_type + "4]" + " RU[" + rule + "] SZ[19] CA[UTF-8] AP[CGoban:3]\n";
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
                e.printStackTrace();
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

    public static boolean getHeaderInfos(){
        Integer board_size;
        int gm;
        String ru;
        // String ?;
        //ha km
        //skip white space Charachter.whitespace ? stringbuilder at i?
        //check ( ; puis get les infos header si pas info skip jusqua [ puis tout les [] sans char entre
        return true;
    }

    public static boolean getGameContent(){
        return true;
    }

    public static boolean parseFile(){
        if ("sgf".equals(getExtension(file)) == false)
            return false;
        //si ( branche ls 1er c'est la 1er et la suite de l'actuelle un fois ) sauter tout les () et ski aussi les () dans () "(((...)))"si brancheS s'attendre a )) a la fin
        int bufferSize = 100;
        char[] buffer = new char[bufferSize];
        StringBuilder file_content = new StringBuilder();
        int charsRead;
        try {//try with ressource ?!??
            Reader reader = new FileReader( file);
            while ((charsRead = reader.read(buffer, 0, bufferSize)) > 0){
                // file_content += new String(buffer);
                file_content.append(buffer, 0, charsRead);
            }
            reader.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("file content == " + file_content);
        //String file content //recuperer tout les fichier avec reader
        //boolean//getHeaderInfos(file_content)//remove les elements parses?
        //si ok init la ArrayuList<Map> //static var
        //boolean get gameContent(file_content)
        return true;
    }

    public static String get_game_rule(){
        return rules;
    }

    public static ArrayList<Map> get_game_moves(){
        return game_moves;
    }

    public static String get_file_name(){
        return file.getName();
    }

    public static void setFile(String absolute_path, String filename){
        file = new File(absolute_path, filename);
    }
}
