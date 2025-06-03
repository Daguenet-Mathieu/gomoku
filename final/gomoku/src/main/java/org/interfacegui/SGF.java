package org.interfacegui;
import org.utils.*;
import org.ast.*;
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
import java.util.Set;
import java.io.IOException;
import java.text.ParseException;

// public enum NodeType{ DATA, OTHER_DATA_TYPE, ...., BRANCH_NODE }

// public abstract class Union //public interface Union{ 
// IMPOSER DE definir set get value
// 
// }

// public class UnionBranch extends Union{
//   Node node;
// }

// public class UnionData extends Union{
//   String Data;
// }

// public class Node{
//    NodeType typeInfo;
//    Union Data;
//    Node next;
// }

public class SGF{

    private static File file;
    private static String rules;
    private static ArrayList<Map> game_moves;
    private static final String[] ignoreSet = new String[] {
        "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AR", "CR",
        "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
        "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
        "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
        "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
        "TW", "AS", "IP", "IY", "SE", "SU", "FF", "BL", "WL"
    };

    // private static final Set<String> ignoreSet = new String[] {
    //     "BM", "DO", "IT", "KO", "MN", "OB", "OW", "TE", "AB", "AW",
    //     "AR", "CR", "DD", "DM", "FG", "GB", "GW", "HO", "LB", "LN",
    //     "MA", "N", "PM", "SL", "SQ", "TR", "UC", "V", "VW", "ST",
    //     "AN", "BR", "BT", "CP", "DT", "EV", "GC", "GN", "ON", "OT",
    //     "PB", "PW", "RE", "RO", "SO", "TM", "US", "WR", "WT", "TB",
    //     "TW", "AS", "IP", "IY", "SE", "SU", "FF"
    // };
    // private static final Set<String> supportedSet = Set.of("KM", "HA", "AP", "CA", "GM", "RU", "SZ", "C", "AE", "PL", "B", "W", "BL", "WL");

    private static final String[] supportedSet = new String[] {"KM", "HA", "AP", "CA", "GM", "RU", "SZ", "C", "AE", "PL", "B", "W", "AB", "AW"};
    private static final String[] listCmdSet = new String[] {"AB", "AW", "AE"};
    private static final String[] rootCmdSet = new String[] {"KM", "HA", "GM", "AP", "CA", "SZ"};
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

    // public static boolean getGameContent(String file_content){
    //     Integer board_size;//changer
    //     int gm;
    //     String ru;
        
    //     //str.charAt(i)
    //     for (Integer i = 0; i < file_content.length(); i++){
    //         String word = "test";//getCurrentWord(String, i);
    //         // if (supportedSet.contains(word)){//file_content.charAT(i);//supportedSet.contains(possibleNullString);
    //         //     //faire action\
    //         //     //si ru //sz //km //... ff// ha// soit remplacer soit rejeter
    //         // }
    //         // else if (ignoreSet.contains(word)){
    //         //     //jump a la fin des values de l'instruciton []...[]
    //         // }
    //         // else
    //         //     ;//erreur et quitter false
    //     }
    //     // String ?;
    //     //ha km
    //     //skip white space Charachter.whitespace ? stringbuilder at i?
    //     //check ( ; puis get les infos header si pas info skip jusqua [ puis tout les [] sans char entre
    //     return true;
    // }

    // public static boolean getGameContent(){
    //     return true;
    // }
    // public static boolean parseFile()
    // {
        // return true ;}

    private static void trimSpace(StringBuilder sb) {
        int i = 0;
        while (i < sb.length() && Character.isWhitespace(sb.charAt(i))) {
            i++;
        }
        if (i > 0) {
            sb.delete(0, i);
    }
}


    private  static Union getNode (CommandType type, String name){
        switch (type)
        {
            case BRANCH:
                return new Node();
            case MOVE:
                return new Node();
            case ARRAY_VALUE:
                return new ArrayValue(name);
            case STRING_VALUE:
                return new StringValue(name);
            case COORD_VALUE:
                return new CoordValue(name);
            case NUM_VALUE:
                return new NumValue(name);
            default:
                return null;
        }       
    }

    private  static String getCommandName(StringBuilder file){

        // // Effacer les 3 premiers caractères
        // file.delete(0, 3);

        // // Effacer le premier caractère
        // file.deleteCharAt(0);
        int index = file.indexOf("[");//proteger pas trouve
        if (index == -1)
            return null;
        String command = file.substring(0, index);
        //verifier si dans supposrted ou unsupported set
        System.out.print("command == " + command);
        file.delete(0, index);
        return command;
    }

    private  static double getValueNum(StringBuilder file){
        int index = file.indexOf("]");
        double value = 0;
        return value;
    }

    private  static Point getValueCoord(StringBuilder file){
        Point value = new Point();
        return value;
    }

    private  static ArrayList<Point> getValueArray(StringBuilder file){
        ArrayList<Point> value = new ArrayList<Point>();
        return value;
    }

    private  static String getValueString(StringBuilder file){

        // // Effacer les 3 premiers caractères
        // file.delete(0, 3);

        // // Effacer le premier caractère
        // file.deleteCharAt(0);
        trimSpace(file);
        int index = file.indexOf("[");
        String value = file.substring(0, index);
        System.out.print("command == " + value);
        file.delete(0, 2 + value.length());
        trimSpace(file);
        return value;
    }


    private static int indexOf(String value, String[] array){
        for (int i = 0; i < array.length; i++)
        {
            if (value.equals(array[i]))
                return i;
        }
        return -1;
    }

    private static void eraseCmd(StringBuilder file){
        trimSpace(file);
        while (file.charAt(0) == '[')
        {
            getCommandName(file);
        }
    }

    private static Node parseMove(StringBuilder file) throws ParseException{
        Node commandList = null;
        while (file.toString().isEmpty() == false && file.charAt(0) != ';')
        {
            String commandName = getCommandName(file);
            if (commandName == null )//check si dans une des listes
                throw new ParseException("invalid syntaxe", 0);
            if (indexOf(commandName, ignoreSet) != -1)
            {
                // Node currentCommand = getNode(getCmdType(), commandName);
            }
            else
            {
                eraseCmd(file);
            }
        }
        return commandList;
    }

    private static Node buildTree(StringBuilder file, int deepth) throws ParseException{ 
        Node tree = new Node();
        Node currentBranch = tree;
        Node currentMove = null;
        boolean branchDone = false;
        if (deepth > 361)
            throw new ParseException("too many branch", 0);
        while (file.toString().isEmpty() == false){
            trimSpace(file);
            if (file.length() == 0)
                return tree;
            char next_char = file.charAt(0);
            file.deleteCharAt(0);
            
            if (next_char == ')'){
                if (deepth == 0)
                    throw new ParseException("invalid syntaxe", 0);
                else
                    return tree;
            }
            else if (next_char == '('){
                // System.out.println("je passe par (")
                Node branch = buildTree(file, deepth + 1);
                if (branch.DataType == null && branch.next == null)
                    throw new ParseException("invalid syntaxe", 0);
                if (currentMove == null)
                {
                    currentBranch.next = branch;
                    currentBranch = branch;
                }
                else
                {
                    currentMove.next = branch;
                    currentMove = branch;
                }
                branchDone = true;
            }
            else if (next_char == ';'){
                if (branchDone == true)
                    throw new ParseException("invalid file format", 0);
                // System.out.println("je passe par (")
                Node newMove = new Node();
                newMove.value = CommandType.MOVE;
                newMove.DataType = parseMove(file);
                if (currentMove != null)
                    currentMove.next = newMove;
                else
                    currentBranch.DataType = newMove;
                currentMove = newMove;
            }
            else
                throw new ParseException("invalid file format", 0);
        }
        return tree;
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
        Node tree;
        try{
            tree = buildTree(file_content, 0);
        
            // res = executeTree();//bool
        }
        catch (ParseException e){
            return false;
        }
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
