package org.interfacegui;
import java.util.ArrayList;
import org.modelai.Game;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.utils.Point;
import javafx.scene.text.Font;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import org.modelai.Candidat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.scene.paint.Color;
import java.util.Comparator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.geometry.Pos;


public class Gomoku
{
    private Pane game_display;
    private ArrayList<Map> _map;
    private Goban goban;
    private GameInfos gameInfos;
    private int _game_infos_size_x;
    public int _game_infos_size_y;
    private  Pane _goban_pane;
    private VBox _game_infos_pane;
    private int _nb_line;
    private int map_index;
    private Rules rule;
    private VBox _end_popin = new VBox();
    private Button _replay;
    private Button _back_home;
    private Home _game_infos = new Home(){};
    private int player_turn = 0;
    private int current_decrement = 0;
    private boolean game_end = false;
    private Timeline gameLoop;
    private Label _end_text = new Label();
    private int _winner = 0;
    private Label game_name;
    private int start_move_time;
    private int end_move_time;
    private ArrayList<Point> candidatsList;
    private ArrayList<Point> hintList;
    private ArrayList<Point> currentForbiddens = new ArrayList<Point>();
    private ArrayList<Integer> whiteTimeList = new ArrayList<Integer>();
    private ArrayList<Integer> blackTimeList = new ArrayList<Integer>(); 
    private int round = 0;
    private Game game;
    private ArrayList<Point> saved;
    private boolean toggleCandidat = false;
    private boolean toggleHint = false;
    private boolean ia_playing = false;
    private ExecutorService executor = null;
    private Future<Point> future = null;
    private ExecutorService executor2 = null;
    private Future<Boolean> future2 = null;
    private boolean forbiddenVisibility = false;
    private Label commentLabel = new Label();    
    private Rules.GameMode playingMode = Rules.GameMode.PLAYING;
    int _width = 0;

    private void setEndGame(){
        System.out.println("coucou");
        int winner = rule.getWinner();
        _end_popin.setVisible(true);
        _end_popin.setManaged(true);
        game_end = true;
        ia_playing = false;
        gameLoop.stop();
        if (winner == 0)
            _end_text.setText("Draw");
        else if (winner == 1)
            _end_text.setText("Black Win");
        else
            _end_text.setText("White Win");

    }

    void updateGameMap(int index){
        for (int i = 0; i < rule.get_board_size(); i++){
            for (int j = 0; j < rule.get_board_size(); j++){
                game.gameMap[i][j] = _map.get(index).get_map()[i][j];
            }
        }
    }

    private Boolean playIa(){
        boolean end = false;
        int mapSize = _map.size();
        int i = 0;
        if (rule.hasIa() == false)
            return false;
        System.out.println("_map.size() " + _map.size());
        for (int mIndex = 0; mIndex < mapSize; mIndex++){
            Map m = _map.get(mIndex);
            ArrayList<Point> points = m.get_prisonners();
            ArrayList<Point> lastMove = m.getLastMove();
            ArrayList<Integer> lastMoveColor = m.getLastMoveColor();
            System.out.println("nb move " + lastMove.size());
            if (mIndex % 2  == 0)
                rule.set_black_prisonners(points.size());
            else
                rule.set_black_prisonners(points.size());
            for (int j = 0; j < lastMove.size(); j++){
                if (lastMoveColor.get(j) != 0)
                {
                    updateGameMap(mIndex);
                    game.move(lastMove.get(j), lastMoveColor.get(j));
                    if (end == false)
                        end = rule.endGame(_map.get(mIndex), lastMove.get(j));
                    if (end == true)
                        return true;
                }
                else{
                    if (m.get_map()[lastMove.get(j).y][lastMove.get(j).x] != 0)
                        game.remove(lastMove.get(j), new ArrayList<Point>(), false);
                }
            }
            for (Point p : points) {
                    game.remove(p, m.get_prisonners(), false);
            }
            updatePlayerTurn();
            game.best_move((i%2==0?2:1), (i%2==0?2:1), true);
            System.out.println("candidat? " + game.m.candidat.lst);
            setCandidats(game.m.candidat.lst, game.m.values, mIndex);
            i++;
        }
        return false;
    }

    private void eraseForbiddens(){
        ArrayList<Point> points = currentForbiddens;
        forbiddenVisibility = false;
        for (Point point : points){
            changeForbiddenVisibility(forbiddenVisibility, point);
        }
        currentForbiddens.clear();
    }

    void changeForbiddenVisibility(boolean visible , Point p) {
        goban.set_stone_status(visible, "#FF0000", p, null);
    }

    void changeHintVisibility(boolean visible) {
        if (hintList == null || hintList.isEmpty()) return;
        for (int i = 0; i < hintList.size(); i++) {
            Point p = hintList.get(i);
            goban.set_stone_status(visible, "#00F0FF", p, String.format("%d", (int)p.val));
        }
    }

    void setHint(ArrayList<Candidat.coord> hint, float[] values) {
        if (rule.hasIa() == true && (hint == null || values == null)) return;

        hintList = new ArrayList<>();
        for (int i = 0; i < hint.size(); i++) {
            hintList.add(new Point(hint.get(i).y, hint.get(i).x));
            hintList.get(hintList.size() - 1).set_val(values[i]);
        }
        hintList.sort(Comparator.comparingDouble(Point::get_val));

        int nb = -1000000000;
        int nb2 = 0;
        int val = 1;
        for (int i = hintList.size() - 1; i >= 0; i--){
            if (nb != -1)
                nb2 = nb;
            nb = (int)hintList.get(i).get_val();
            if ((int)nb < (int)nb2)
                val = hintList.size() - i;
            hintList.get(i).set_val((float)val);
        }
    }


    void changeCandidatVisibility(boolean visible) {
        ArrayList<Point> currentCandidats = _map.get(map_index).getCandidatsList();
        if (currentCandidats == null || currentCandidats.isEmpty()) return;

        for (int i = 0; i < currentCandidats.size(); i++) {
            Point p = currentCandidats.get(i);
            if (p.val < 0)
                goban.set_stone_status(visible, "#FF0000", p, String.format("%.0f", p.val));
            else{
                goban.set_stone_status(visible, "#00FF00", p, String.format("%.0f", p.val));
            }
        }
        if (visible == false){
            goban.updateFromMap(_map.get(map_index));
        }
    }

    void setCandidats(ArrayList<Candidat.coord> candidats, float[] values, int index) {
        if (rule.hasIa() == false || candidats == null || values == null || game.val == null) return;
        candidatsList = new ArrayList<>();
        changeCandidatVisibility(false);
        for (int i = 0; i < values.length; i++) {
            candidatsList.add(new Point(candidats.get(i).y, candidats.get(i).x));
            candidatsList.get(candidatsList.size() - 1).set_val(values[i]);
        }
        _map.get(index).setCandidatsList(candidatsList);
    }

    void showCandidats() {
        if (candidatsList == null)
            return ;
        for (Point p : candidatsList) {
            System.err.println("Score: " + p.val + " -> Point: " + p);
        }
    }

    public void reset_gomoku(){
        gameInfos.getResultsBox().setVisible(true);
        gameInfos.getResultsBox().setManaged(true);
        _map.clear();
        _map.add(new Map(_nb_line));
        saved.clear();
        playingMode = _game_infos.getGameMode();
        map_index = 0;
        round = 0;
        gameInfos.setPLayTurn(round);
        init_rules(_game_infos.get_rules(), _game_infos.get_board_size());
        goban.updateFromMap(_map.get(_map.size() - 1));
        gameInfos.clear();
        gameInfos.reset_infos(_game_infos);
        createDelayedGameLoop();
        goban.remove_score();
        _end_popin.setVisible(false);
        _end_popin.setManaged(false);
        player_turn = 0;
        current_decrement = 0;
        game_end = false;
        if (rule.hasIa()){
            game = new Game(_game_infos.get_rules(), rule.get_board_size());
            game.reset_minmax();
        }
        _winner = 0;
        gameInfos.set_black_prisonners("0");
        gameInfos.set_white_prisonners("0");
        ia_playing = false;
        game.tree_config(_game_infos.getLevel());
        if (rule.hasPass() == false){
            gameInfos.getPassButton().setVisible(false);
            gameInfos.getPassButton().setManaged(false);
        }
        changeCandidatVisibility(false);
        changeHintVisibility(false);
        eraseForbiddens();
        forbiddenVisibility = false;
        toggleCandidat = false;
        toggleHint = false;
    }


    private int get_average(ArrayList<Integer> list){
        if (list.isEmpty()) return 0;
        int sum = 0;
        for (int val : list) sum += val;
        return sum / list.size();
    }

    public void createDelayedGameLoop() {
        gameLoop = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            if (future2 == null && (_game_infos.get_black_player_type() == 0 || _game_infos.get_white_player_type() == 0)){
                gameInfos.getUndoButton().setManaged(true);
                gameInfos.getUndoButton().setVisible(true);

            }

        if (rule.hasIa() == true)
        {
            try {
                if (player_turn == 0 && _game_infos.get_black_player_type() == 1){//faire une fct
                    if (ia_playing == false){
                            executor = Executors.newSingleThreadExecutor();
                            future = executor.submit(() -> {
                                return game.best_move(player_turn+1, player_turn+1, true);
                            });
                        ia_playing = true;
                    }
                    else if (future.isDone()){
                        playMove(future.get());
                        executor.shutdown();
                        executor = null;
                        setCandidats(game.m.candidat.lst, game.m.values, map_index);
                        ia_playing = false;
                    }
                }
                else if (player_turn == 1 && _game_infos.get_white_player_type() == 1){
                    if (ia_playing == false)
                    {
                        executor = Executors.newSingleThreadExecutor();
                        future = executor.submit(() -> {
                            return game.best_move(player_turn+1, player_turn+1, true);
                        });
                        ia_playing = true;
                    }
                    else if (future.isDone()){
                        playMove(future.get());
                        executor.shutdown();
                        executor = null;
                        setCandidats(game.m.candidat.lst, game.m.values, map_index);
                        ia_playing = false;
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("error launching IA think : " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
        if (future2 != null && future2.isDone()) {
            executor2.shutdown();
            try {
                System.out.println("furture2 rs == " + future2.get());
                if (future2.get() == true) {
                    setEndGame();
                }
            }
            catch (Exception e) {
                System.err.println("error launching IA think : " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
            future2 = null;
        }
        if (player_turn != current_decrement){
            int t = end_move_time - start_move_time;
            if (current_decrement == 0)
                blackTimeList.add(t);
            else
                whiteTimeList.add(t);
            current_decrement = current_decrement == 0?1:0;
            gameInfos.set_last_move_time(t);
            start_move_time = 0;
            end_move_time = 0;
            return ;
        }
        gameInfos.set_average_white_time(get_average(whiteTimeList));
        gameInfos.set_average_black_time(get_average(blackTimeList));
        if (player_turn == 0)
            gameInfos.sub_black_time(10);
        else
            gameInfos.sub_white_time(10);
        end_move_time += 10;
        if (gameInfos.get_black_time() <= 0 || gameInfos.get_white_time() <= 0){
            gameLoop.stop();
            game_end = true;
            ia_playing = false;
            _winner = (gameInfos.get_black_time() <= 0) ? 2 : 1;
            String res = _winner == 1? "black" : "white";
            _end_text.setText(res + " win");
            _end_popin.setVisible(true);
            _end_popin.setManaged(true);
        }
        });
        gameLoop.getKeyFrames().add(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }


    public void setAllLabelsColor(Parent parent, Color color) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(color);
            } else if (node instanceof Parent) {
                setAllLabelsColor((Parent) node, color);
            }
        }
    }

    private void setPlayerColor(){
        if (player_turn == 0){
            gameInfos.getBlackBox().setBackground(new Background(new BackgroundFill(Color.web("#2F4F4F"), null, null)));
            gameInfos.getWhiteBox().setBackground(new Background(new BackgroundFill(Color.web("#ADBAC0"), null, null)));
            setAllLabelsColor(gameInfos.getBlackBox(), Color.WHITE);
            setAllLabelsColor(gameInfos.getWhiteBox(), Color.BLACK);
        }
        else{
            gameInfos.getBlackBox().setBackground(new Background(new BackgroundFill(Color.web("#ADBAC0"), null, null)));
            gameInfos.getWhiteBox().setBackground(new Background(new BackgroundFill(Color.web("#2F4F4F"), null, null)));
            setAllLabelsColor(gameInfos.getBlackBox(), Color.BLACK);
            setAllLabelsColor(gameInfos.getWhiteBox(), Color.WHITE);
        }
    }

    private void init_rules(String rules_type, int boardSize){
        rules_type = rules_type.toLowerCase();
        switch (rules_type){
            case "pente" :
                rule = new PenteRules();
                break;
            case "renju" :
                rule = new RenjuRules();
                break;
            case "go":
                rule = new GoRules();
                break;
            default:
                rule = new GomokuRules();
        }
        rule.setBoardSize(boardSize);
    }

    public void killIa(){
        if (gameLoop != null)
            gameLoop.stop();
        if (executor != null)
            executor.shutdown();
    }

    private void playMove(Point point){
        if (map_index < (_map.size() - 1) || !rule.isValidMove(point, _map)){
            return ;
        }
        if (rule.getGameMode() == Rules.GameMode.DEATH_MARKING){
            ArrayList<Point> deadStones = ((GoRules)rule).getDeadStones();
            Map currentMap = _map.get(_map.size() - 1);
            int color = currentMap.get_map()[point.y][point.x];
            int newColor = (color == 1 || color == 2) ? color + 2 : color - 2;
            for (Point p : deadStones){
                currentMap.get_map()[p.y][p.x] = newColor;
            }
            goban.updateFromMap(currentMap);
            return ;
        }
        if (rule.getGameMode() != Rules.GameMode.PLAYING){
            return ;
        }
        changeCandidatVisibility(false);
        toggleCandidat = false;
        changeHintVisibility(false);
        hintList = null;
        toggleHint = false;
        _map.get(_map.size() -1);
        _map.add(new Map(_map.get(_map.size() - 1)));
        _map.get(_map.size() - 1).addMove(point, _map.size() % 2 + 1);
        map_index = _map.size() - 1;
        if (rule.hasIa() == true)
        {
            game.gameMap[point.y][point.x] = player_turn + 1;
            game.move(point, player_turn+1);
        }
        _map.get(_map.size() -1);
        rule.check_capture(point, _map.get(_map.size() - 1));

        if ((rule instanceof GomokuRules) == false)
        {
            ArrayList<Point> points = rule.GetCapturedStones(point, _map.get(_map.size() - 1));
            for (Point p : points) {
                if (rule.hasIa() == true)
                {
                    game.gameMap[p.y][p.x] = 0;
                    game.remove(p, _map.get(_map.size() - 1).get_prisonners(), false);
                }
            }
            points = rule.get_prisonners();
            _map.get((_map.size()-1)).remove_prisonners(points);
            if (player_turn == 0)
                _map.get((_map.size()-1)).addBlackPrisonners(points.size());
            else
                _map.get((_map.size()-1)).addWhitePrisonners(points.size());
            if (points != null && points.size() > 0){
                display_nb_prisonners();
            }
            _map.get((_map.size()-1)).set_prisonners(points);
        }
        _map.get((_map.size()-1)).set_color(player_turn);

        if (rule.endGame(_map.get(_map.size() - 1), point)){
            int winner = rule.getWinner();
            _end_popin.setVisible(true);
            _end_popin.setManaged(true);
            game_end = true;
            ia_playing = false;
            gameLoop.stop();
            if (winner == 0)
                _end_text.setText("Draw");
            else if (winner == 1)
                _end_text.setText("Black Win");
            else
                _end_text.setText("White Win");
        }
        goban.updateFromMap(_map.get(_map.size() -1));
        if (player_turn == 0){
            round++;
            gameInfos.setPLayTurn(round);
        }
        updatePlayerTurn();
        setPlayerColor();
    }

    private void updatePlayerTurn(){
        player_turn ^= 1;
    }

    private void display_nb_prisonners(){
        gameInfos.set_black_prisonners(Integer.toString( _map.get((_map.size()-1)).getBlackPrisonners()));
        gameInfos.set_white_prisonners(Integer.toString( _map.get((_map.size()-1)).getWhitePrisonners()));

    }

    private void undoMove(){
        if (_map.size() < 2 || map_index < _map.size() - 1)
        {
            return ;
        }
        changeCandidatVisibility(false);
        changeHintVisibility(false);
        eraseForbiddens();
        forbiddenVisibility = false;
        toggleCandidat = false;
        toggleHint = false;
        map_index -= 1;
        if (rule.hasIa() == true){
            ArrayList<Point> coord = _map.get(_map.size() - 1).getLastMove();
            for (int i = 0; i < coord.size(); i++){
                ArrayList<Point> prisonners;
                if (i == 0)
                    prisonners = _map.get(_map.size() - 1).get_prisonners();
                else
                    prisonners = new ArrayList<Point>();
                game.remove(coord.get(i), prisonners, true);
            }
        }
        _map.remove(_map.size() - 1);
        for (int i = 0; i < rule.get_board_size(); i++){
            for (int j = 0; j < rule.get_board_size(); j++){
                game.gameMap[i][j] = _map.get(_map.size() - 1).get_map()[i][j];
            }
        }
        goban.updateFromMap(_map.get(_map.size() - 1));
        player_turn ^= 1;
        rule.set_black_prisonners(_map.get((_map.size()-1)).getBlackPrisonners());
        rule.set_white_prisonners(_map.get((_map.size()-1)).getWhitePrisonners());
        display_nb_prisonners();
    }


    public Button getBackHomeButton(){
        return gameInfos.getBackHomeButton();
    }

    public Gomoku(int heigh, int width, Home game_infos){
        _game_infos = game_infos;
        _width = width;
        if (_game_infos.getRuleInstance() != null)
            rule = _game_infos.getRuleInstance();
        else
            init_rules(_game_infos.get_rules(), _game_infos.get_board_size());
        _nb_line = rule.get_board_size();
        gameInfos = new GameInfos(heigh, _game_infos_size_x, game_infos);
        playingMode = game_infos.getGameMode();
        if (playingMode == Rules.GameMode.PLAYING){
            System.out.println("init game rule : " + game_infos.get_rules());
            game = new Game(game_infos.get_rules(), rule.get_board_size());
            game.reset_minmax();
            if (game_infos.get_black_player_type() == 0 && game_infos.get_white_player_type() == 0)
                game.tree_config(4);
            else
                game.tree_config(game_infos.getLevel());
        }
        map_index = 0;
        _map = new ArrayList<Map>();
        goban = new Goban(heigh, width - _game_infos_size_x, rule.get_board_size());
        commentLabel.setManaged(false);
        commentLabel.setVisible(false);
        if (game_infos.getSgfMap() != null){
            _map = game_infos.getSgfMap();
            if (playingMode == Rules.GameMode.PLAYING){
                executor2 = Executors.newSingleThreadExecutor();
                future2 = executor2.submit(() -> playIa());
            }
        }
        else
            _map.add(new Map(_nb_line));
        if (playingMode == Rules.GameMode.LEARNING){
            gameInfos.getBackHomeButton().setVisible(true);
            gameInfos.getBackHomeButton().setManaged(true);
            gameInfos.get_last_move_time().setVisible(false);
            gameInfos.get_last_move_time().setManaged(false);
            gameInfos.get_average_black_time().setManaged(false);
            gameInfos.get_average_black_time().setVisible(false);
            gameInfos.get_average_white_time().setManaged(false);
            gameInfos.get_average_white_time().setVisible(false);
            gameInfos.getCandidatsButton().setVisible(false);
            gameInfos.getCandidatsButton().setManaged(false);
            gameInfos.getHintButton().setVisible(false);
            gameInfos.getHintButton().setManaged(false);
            gameInfos.getResignButton().setManaged(false);
            gameInfos.getResignButton().setVisible(false);
            gameInfos.getExportButton().setManaged(false);
            gameInfos.getExportButton().setVisible(false);
            gameInfos.get_black_time_label().setManaged(false);
            gameInfos.get_black_time_label().setVisible(false);
            gameInfos.get_white_time_label().setManaged(false);
            gameInfos.get_white_time_label().setVisible(false);
            _map.remove(0);
            goban.updateFromMap(_map.get(0));
            commentLabel.setManaged(true);
            commentLabel.setVisible(true);
            commentLabel.setText(_map.get(map_index).getComment());
            commentLabel.setMaxWidth(Double.MAX_VALUE);
            commentLabel.setAlignment(Pos.CENTER);
            commentLabel.setPrefHeight(120);
            commentLabel.setMinHeight(120);
            commentLabel.setMaxHeight(120);
            rule.setGameMode(Rules.GameMode.LEARNING);
        }
        saved = new ArrayList<Point>();
        game_display = new Pane();
        _replay = new Button("Replay");
        _back_home = new Button("Back Home");
        game_name = new Label(game_infos.get_rules());
        _end_popin.setVisible(false);
        _end_popin.setManaged(false);
        _end_popin.setLayoutX(10);
        _end_popin.setLayoutY(10);
        _back_home.setLayoutY(heigh * 0.8);
        _end_popin.setFillWidth(true);
        _end_popin.getChildren().addAll(_end_text, _replay, _back_home);
        _game_infos_size_x = width / 4;
        _game_infos_size_y = heigh;
        gameInfos.getUndoButton().setManaged(false);
        gameInfos.getUndoButton().setVisible(false);
        if (_game_infos.get_black_player_type() == 1 && _game_infos.get_white_player_type() == 1){
            gameInfos.getBackHomeButton().setVisible(true);
            gameInfos.getBackHomeButton().setManaged(true);
            gameInfos.getUndoButton().setManaged(false);
            gameInfos.getUndoButton().setVisible(false);
            gameInfos.getHintButton().setManaged(false);
            gameInfos.getHintButton().setVisible(false);
            gameInfos.getResignButton().setManaged(false);
            gameInfos.getResignButton().setVisible(false);
        }
        _goban_pane = goban.get_goban();
        _game_infos_pane = gameInfos.getGameInfos();
        _game_infos_pane.getChildren().add(0, game_name);
        _game_infos_pane.getChildren().add(0, _end_popin);
        setPlayerColor();
            DoubleBinding fontSizeBinding = (DoubleBinding) Bindings.min(
                _game_infos_pane.widthProperty().multiply(0.1),
                _game_infos_pane.heightProperty().multiply(0.1)
            );

        game_name.fontProperty().bind(Bindings.createObjectBinding(
                () -> new Font("Arial", fontSizeBinding.get()),
                fontSizeBinding
            ));
        _goban_pane.setLayoutX(_game_infos_size_x);
        VBox mainVBox = new VBox();
        HBox hbox = new HBox();
        hbox.getChildren().addAll(_game_infos_pane, _goban_pane);
        mainVBox.getChildren().addAll(commentLabel, hbox);
        game_display.getChildren().add(mainVBox);
        if (playingMode == Rules.GameMode.PLAYING)
            createDelayedGameLoop();
        if (rule.hasPass() == false){
            gameInfos.getPassButton().setVisible(false);
            gameInfos.getPassButton().setManaged(false);
        }
        if (rule.hasIa() == false){
            gameInfos.getHintButton().setVisible(false);
            gameInfos.getHintButton().setManaged(false);
            gameInfos.getCandidatsButton().setVisible(false);
            gameInfos.getCandidatsButton().setManaged(false);

        }
        gameInfos.getResignButton().setOnAction(event -> {
                gameLoop.stop();
                game_end = true;
                ia_playing = false;
                _winner = player_turn;
                String res = _winner == 1? "black" : "white";
                _end_text.setText(res + " win");
                _end_popin.setVisible(true);
                _end_popin.setManaged(true);
            
        });

        gameInfos.getUndoButton().setOnAction(event -> {
            if (rule.getGameMode() == Rules.GameMode.ENDGAME)
                return ;
            if (rule.undo() == false)
                return ;
            goban.remove_score();
            undoMove();
            if (rule.hasIa() && (_game_infos.get_black_player_type() == 1 || _game_infos.get_white_player_type() == 1) && _map.size() > 1)
                undoMove();
        });
        gameInfos.getExportButton().setOnAction(event -> {
            SGF.createSgf(_map, rule.getGameType());
        });

        gameInfos.getPrevButton().setOnAction(event -> {
            if (map_index > 0){
                changeCandidatVisibility(false);
                changeHintVisibility(false);
                eraseForbiddens();
                forbiddenVisibility = false;
                toggleCandidat = false;
                toggleHint = false;
                map_index--;
                goban.updateFromMap(_map.get(map_index));
                if (_map.get(map_index).getComment() != null && _map.get(map_index).getComment().isEmpty() == false)
                {
                    commentLabel.setManaged(true);
                    commentLabel.setVisible(true);
                    commentLabel.setText(_map.get(map_index).getComment());
                    commentLabel.setMaxWidth(Double.MAX_VALUE);
                    commentLabel.setAlignment(Pos.CENTER);
                    updateGameDisplay(_game_infos_size_y, _width);
                }
                else
                {
                    commentLabel.setManaged(false);
                    commentLabel.setVisible(false);
                }
                gameInfos.set_black_prisonners(Integer.toString( _map.get(map_index).getBlackPrisonners()));
                gameInfos.set_white_prisonners(Integer.toString( _map.get(map_index).getWhitePrisonners()));
            }
        });
        gameInfos.getNextButton().setOnAction(event -> {
            if (map_index < _map.size() - 1){
                changeCandidatVisibility(false);
                changeHintVisibility(false);
                eraseForbiddens();
                forbiddenVisibility = false;
                toggleCandidat = false;
                toggleHint = false;
                map_index++;
                goban.updateFromMap(_map.get(map_index));
                gameInfos.set_black_prisonners(Integer.toString( _map.get(map_index).getBlackPrisonners()));
                gameInfos.set_white_prisonners(Integer.toString( _map.get(map_index).getWhitePrisonners()));
                if (_map.get(map_index).getComment() != null && !_map.get(map_index).getComment().isEmpty()) {
                    commentLabel.setManaged(true);
                    commentLabel.setVisible(true);
                    commentLabel.setText(_map.get(map_index).getComment());
                    commentLabel.setMaxWidth(Double.MAX_VALUE);
                    commentLabel.setAlignment(Pos.CENTER);
                    updateGameDisplay(_game_infos_size_y , _width);
                }
                else
                {
                    commentLabel.setManaged(false);
                    commentLabel.setVisible(false);
                }

            }
        });
        gameInfos.getCandidatsButton().setOnAction(event -> {
            if (ia_playing == true)
                return ;
            toggleCandidat = toggleCandidat == true? false : true;
            changeCandidatVisibility(toggleCandidat);
            changeHintVisibility(false);
            eraseForbiddens();
            forbiddenVisibility = false;
            toggleHint = false;
            if (toggleCandidat == false){
                goban.updateFromMap(_map.get(map_index));
            }
        });

        gameInfos.getHintButton().setOnAction(event -> {
            if (rule.hasIa() == false || ia_playing || game_end)
                return ;
            toggleHint = toggleHint == true? false : true;
            if (hintList == null){
                game.best_move(player_turn+1, player_turn+1, true);
                setHint(game.m.candidat.lst, game.m.values);
            }
            changeCandidatVisibility(false);
            changeHintVisibility(toggleHint);
            eraseForbiddens();
            forbiddenVisibility = false;
            toggleCandidat = false;
            if (toggleHint == false){
                goban.updateFromMap(_map.get(map_index));
            }

        });
        gameInfos.getForbiddeButton().setOnAction(event -> {
            if (ia_playing)
                return ;
            changeCandidatVisibility(false);
            changeHintVisibility(false);
            ArrayList<Point> points;
            if (playingMode == Rules.GameMode.LEARNING)
                points = rule.get_forbiden_moves(_map, map_index, 1);
            else
                points = rule.get_forbiden_moves(_map, map_index, (map_index % 2) + 1);
            forbiddenVisibility = forbiddenVisibility == false;
            toggleCandidat = false;
            toggleHint = false;
            currentForbiddens = points;
            for (Point point : points){
                changeForbiddenVisibility(forbiddenVisibility, point);
            }
            if (forbiddenVisibility == false)
                currentForbiddens.clear();
            if (forbiddenVisibility == false){
                goban.updateFromMap(_map.get(map_index));
            }
        });

        gameInfos.getPassButton().setOnAction(event -> {
            GoRules r = (GoRules)rule;
            String res;
            if (r.pass())
                updatePlayerTurn();
            if (r.getGameMode() == Rules.GameMode.ENDGAME && gameLoop != null){
                int blackScore = r.getBlackScore();
                int whiteScore = r.getWhiteScore();
                if (whiteScore > blackScore)
                    res = "white win";
                else if (whiteScore < blackScore)
                    res = "black win";
                else
                    res = "jigo";
                _end_text.setText(res);
                game_end = true;
                gameLoop.stop();
                _end_popin.setVisible(true);
                _end_popin.setManaged(true);
                return ;
            }
            Map newMap = new Map(_map.get(_map.size() - 1));
            _map.add(newMap);
            map_index++;
            if (rule.getGameMode() == Rules.GameMode.COUNTING){
                ((GoRules)rule).init_prisonners(_map.get(_map.size() - 1));
                ArrayList<Point> tmp = ((GoRules)rule).getWhitePrisonnersList();
                for (Point p : tmp)
                    goban.modify_score(p, Color.WHITE);
                tmp = ((GoRules)rule).getBlackPrisonnersList();
                for (Point p : tmp)
                    goban.modify_score(p, Color.BLACK);
                gameInfos.setBlackResults("black res: " + r.getBlackScore());
                gameInfos.setWhiteResults("white res: " + r.getWhiteScore());
                gameInfos.getResultsBox().setVisible(true);
                gameInfos.getResultsBox().setManaged(true);
                return ;
            }
        });


            goban.get_goban().setOnMouseClicked(event -> {
            if (game_end)
                return ;
            int margin_w = goban.get_margin_width();
            int margin_h = goban.get_margin_height();
            int square = goban.getSquareSize();
            int goban_size = square * (_nb_line);
            int width_allowed_margin;
            int height_allowed_margin;
            if (margin_w < square / 2)
                width_allowed_margin = margin_w;
            else
                width_allowed_margin = square/2;
            if (margin_h < square / 2)
                height_allowed_margin = margin_h;
            else
                height_allowed_margin = square/2;
            double x = event.getX();
            double y = event.getY();
            x -= margin_w;
            y -= margin_h;
            if (x < 0)
                x += width_allowed_margin;
            if (y < 0)
                y += height_allowed_margin;
            if (x > goban_size)
                x -= width_allowed_margin;
            if (y > goban_size)
                y -= height_allowed_margin;          
            x/= square;
            y/= square;
            x = Math.round(x);
            y = Math.round(y);
            if (x < 0 || x >= rule.get_board_size() || y < 0 || y >= rule.get_board_size())
                return ;
            Point new_move = new Point((int)x, (int)y);
            saved.add(new_move);
            playMove(new_move);
        });

    }

    public void updateGameDisplay(int new_y, int new_x){
        _width = new_x;
        _game_infos_size_x = new_x / 4;
        _game_infos_size_y = new_y;
        gameInfos.updateGameInfo(new_y, _game_infos_size_x);
        double labelHeight = commentLabel.prefHeight(commentLabel.getMaxWidth());
        if (commentLabel.isVisible() == false)
            labelHeight = 0;
        goban.updateGoban(new_y - (int)labelHeight, new_x - _game_infos_size_x);
        _goban_pane.setLayoutX(_game_infos_size_x);
    }

    public Pane getGameDisplay(){
        _goban_pane.setLayoutX(_game_infos_size_x);
        return game_display;
    }


    public Button get_home_button(){
        return _back_home;
    }

    public Button get_replay_button(){
        return _replay;
    }

}
