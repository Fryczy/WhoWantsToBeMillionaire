import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * A program Ebből az osztályból fut a main metódus, a kezdőképernyő, és a kérdések megjelenítésért felelős ablak
 */
public class App extends Application {
    static ObservableList<Question> questions = FXCollections.observableArrayList();
    static int QID = 0;
    static Logger logger = Logger.getLogger("WhoWantsToBEMillionaire");
    static Stage mainwindow;
    static XML xml = new XML();
    static Properties propi;

    enum gender {man, woman}

    ;

    String Playername;
    String Playergender;
    int Playerage;
    int score = 0;

    /**
     *A GUI belépési pontja
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        logger.severe("XML tasks");


        xml.XMLCreator("Questions.xml");
/*      for (Question q:questions) {                       // first data
            xml.WriteDOM(q);
        }
        xml.SaveToDOM();
  */
        questions = xml.GetAllQuestions();

        logger.severe("End of XML tasks");

        propInit();
        mainwindow = primaryStage;
        mainwindow.setTitle("Who wants to be a millionaire?");

        //főablak
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button New = new Button("New Question");
        New.setOnAction(e -> {                                   //új kérdés felvétele
            Question.AddNewQuestion();
        });
        Button Game = new Button("Let's play!");
        Game.setOnAction(e -> {
            mainwindow.close();
            Player();
        });

        vbox.getChildren().addAll(New, Game);

        Scene scene1 = new Scene(vbox, 400, 250, Color.GREEN);


        mainwindow.setScene(scene1);


        mainwindow.show();
        mainwindow.setOnCloseRequest(e -> UpdateXML(xml));
    }

    /**
     * A Let's play gomb megnyomás után megjelenő ablak
     */
    private void Player() {
        Stage player = new Stage();
        player.setTitle("Player");
        Scene scene;

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField name = new TextField(propGetName());
        Label namelabel = new Label("Name");

        TextField age = new TextField(propGetAge());
        Label agelab = new Label("Age");
        ComboBox<String> gendercb = new ComboBox<>();
        gendercb.getItems().addAll("man", "woman");
        gendercb.getSelectionModel().select(propGetGender());
        Label genderlab = new Label("Gender");
        Label score = new Label("Score:");
        Label points = new Label(propGetScore());

        Button btn1 = new Button("Play");
        btn1.setOnAction(e -> {


            try {

                if (gendercb.getSelectionModel().getSelectedItem().equals("") || name.getText().equals("") || age.getText().equals("")) {
                    throw new NullPointerException();
                }

                Playername = name.getText();
                propi.setProperty("Name",Playername);
                Playerage = Integer.parseInt(age.getText());
                propi.setProperty("Age",((Integer)Playerage).toString());
                Playergender = gendercb.getSelectionModel().getSelectedItem();
                propi.setProperty("Gender",(Playergender));

                propWrite();

                questionLayout(player);

            } catch (NullPointerException ex) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Empty Fields");
                alert.setHeaderText("Warning!");
                alert.setContentText("Fill all fields!");
                alert.showAndWait();
            }


        });

        grid.add(namelabel, 1, 1);
        grid.add(agelab, 1, 3);
        grid.add(genderlab, 1, 5);
        grid.add(score, 2, 1);


        grid.add(name, 1, 2);
        grid.add(age, 1, 4);
        grid.add(gendercb, 1, 6);
        grid.add(points, 3, 1);

        grid.add(btn1, 2, 4);

        scene = new Scene(grid, 600, 500);

        player.setScene(scene);
        player.show();

    }


    /**
     * A játékfelületét futtatja és eseménykezelőket tartalmaz, a legutolsó kérdés után nem önmagát hívja meg hanem egy új ablakot
     * @param game
     */
    private void questionLayout(Stage game) {
        game.setTitle("Game");
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        RadioButton radio1 = new RadioButton();
        RadioButton radio2 = new RadioButton();
        RadioButton radio3 = new RadioButton();
        RadioButton radio4 = new RadioButton();
        Label labelfirst = new Label();
        // Label labelresponse = new Label();


        Question q = questions.get(QID);


        labelfirst.setText(q.question);
        String A = q.answers.False1;
        String B = q.answers.False2;
        String C = q.answers.False3;
        String D = q.answers.TrueAnswer;
        List<String> choices = Arrays.asList(A, B, C, D);
        Collections.shuffle(choices);

        radio1.setText(choices.get(0));
        radio2.setText(choices.get(1));
        radio3.setText(choices.get(2));
        radio4.setText(choices.get(3));
        Button button = new Button("Submit");
        ToggleGroup question1 = new ToggleGroup();

        radio1.setToggleGroup(question1);
        radio2.setToggleGroup(question1);
        radio3.setToggleGroup(question1);
        radio4.setToggleGroup(question1);

        button.setDisable(true);

        radio1.setOnAction(e -> button.setDisable(false));
        radio2.setOnAction(e -> button.setDisable(false));
        radio3.setOnAction(e -> button.setDisable(false));
        radio4.setOnAction(e -> button.setDisable(false));

        button.setOnAction(e ->
                {

                    if ((radio1.isSelected() && q.answers.TrueAnswer.equals(radio1.getText())) ||
                            (radio2.isSelected() && q.answers.TrueAnswer.equals(radio2.getText())) ||
                            (radio3.isSelected() && q.answers.TrueAnswer.equals(radio3.getText())) ||
                            (radio4.isSelected() && (q.answers.TrueAnswer.equals(radio4.getText())))) {

                        score++;

                        String musicFile = "correctanswer.mp3";     // For example
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        MediaPlayer correctMusic = new MediaPlayer(sound);
                        correctMusic.play();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Correct Answer");
                        alert.setHeaderText("Congratulations!!");
                        alert.setContentText("It was the correct answer");

                        alert.showAndWait();
                        button.setDisable(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(":C");
                        alert.setHeaderText("Wrong Answer");

                        String File = "wronganswer.mp3";
                        Media sound1 = new Media(new File(File).toURI().toString());
                        MediaPlayer wrongMusic = new MediaPlayer(sound1);
                        wrongMusic.play();
                        alert.showAndWait();
                        button.setDisable(true);
                    }


                    QID++;

                    if (QID == (questions.size())) {
                        SumScore(game);
                    } else {

                        questionLayout(game);
                    }
                }
        );


        grid.add(labelfirst, 2, 1);
        grid.add(radio1, 2, 3);
        grid.add(radio2, 2, 4);
        grid.add(radio3, 2, 5);
        grid.add(radio4, 2, 6);
        grid.add(button, 3, 8);


        Scene scene = new Scene(grid, 500, 400);

        game.setScene(scene);
        game.show();

    }

    /**
     * Létrehozza azta felületet, ami a végén kiírja a játékos által szerzett pontokat
     * @param s
     */
    private void SumScore(Stage s) {
        Stage window = new Stage();
        Scene scene;

        Label scorelab = new Label("Your Score:");
        Label pointslab = new Label(((Integer) score).toString());
        propi.setProperty("Score",((Integer) score).toString());
        propWrite();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(scorelab, 0, 0);
        grid.add(pointslab, 1, 0);


        scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        s.close();
        window.showAndWait();


    }

    /**
     * A kérdések hozzáadása után frissíti az XML fájlunkat, az ablak bezárása után is lefut ez a metódus
     * @param xml
     */
    static public void UpdateXML(XML xml) {
        logger.severe("Refesh Data in XML");
        xml.ClearXMLFile();
        for (Question q : questions) {
            xml.WriteDOM(q);
        }

        xml.SaveToDOM();
    }

    public static void main(String[] args) {
        questions = FXCollections.observableArrayList(new Question("Cukor?", "Igen", "Nem", "Nem", "Nem"),
                new Question("Jó?", "Igen", "Nem", "Nem", "Nem"),
                new Question("Macciii?", "Igen", "Nem", "Nem", "nem"));
        launch(args);

    }

    /**
     * A inicilaziálja a propretyt és létrehozza a fájlt
     */
   static public void propInit() {
        propi = new Properties();
        propi.setProperty("Name", "Frici");
        propi.setProperty("Age", "22");
        propi.setProperty("Gender","man");
        propi.setProperty("Score","0");


        FileInputStream fis;
        try {                                                       //próbálja megnyitni a fájlt
            fis = new FileInputStream("player.prop");
        } catch (FileNotFoundException e) {                         //ha nem találja akkor létrehozza
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(new File("player.prop"));
                fos.close();
                fis = new FileInputStream("player.prop");
            } catch (FileNotFoundException e1) {                                          //ha ezután sem találja akkor kivételt dob
                logger.severe("player.prop missing");
            } catch (IOException e2) {
                logger.severe("I/O Failure");
            }
        }
        try {
            fis = new FileInputStream("player.prop");
            propi.load(fis);
            fis.close();
        } catch (IOException e) {
            logger.severe("I/O Failure");
        }

    }
    static public  void propWrite()
    {
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(new File("player.prop"));
            propi.store(fos, "Previous Setting");

            fos.close();
        }
        catch(FileNotFoundException e){
            logger.severe("player.prop not found");
        }
        catch (IOException e1) {
            logger.severe("I/O Error");
        }
    }
static public String propGetName() { return propi.getProperty("Name");}
static public String propGetAge () {return propi.getProperty("Age");}
static public String propGetGender () {return propi.getProperty("Gender");}
static  public String propGetScore () {return  propi.getProperty("Score");}

}



