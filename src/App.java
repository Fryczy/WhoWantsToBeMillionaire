import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class App extends Application {
    static ObservableList<Question> questions;
    static int QID = 0;
    static Logger logger;


    String musicFile = "correctanswer.mp3";     // For example
    Media sound = new Media(new File(musicFile).toURI().toString());
    MediaPlayer correctMusic = new MediaPlayer(sound);

    String File = "wronganswer.mp3";
    Media sound1 = new Media(new File(File).toURI().toString());
    MediaPlayer wrongMusic = new MediaPlayer(sound1);

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Test Question 1");

        VBox layout = new VBox(5);
        RadioButton radio1 = new RadioButton();
        RadioButton radio2 = new RadioButton();
        RadioButton radio3 = new RadioButton();
        RadioButton radio4 = new RadioButton();
        Label labelfirst = new Label();
        Label labelresponse = new Label();


        Question q = DisplayQuestion(QID);

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
                    QID++;

                    if ((radio1.isSelected() && q.answers.TrueAnswer.equals(radio1.getText())) ||
                            (radio2.isSelected() && q.answers.TrueAnswer.equals(radio2.getText())) ||
                            (radio3.isSelected()&&q.answers.TrueAnswer.equals(radio3.getText())) ||
                            (radio4.isSelected()&&(q.answers.TrueAnswer.equals(radio4.getText())))) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Correct Answer");
                        alert.setHeaderText("Congratulations!!");
                        alert.setContentText("It was the correct answer");
                        correctMusic.play();
                        alert.showAndWait();
                        button.setDisable(true);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle(":C");
                        alert.setHeaderText("Wrong Answer");
                        wrongMusic.play();
                        alert.showAndWait();
                        button.setDisable(true);
                    }
                    start(primaryStage);
                }
        );


        layout.getChildren().addAll(labelfirst, radio1, radio2, radio3, radio4, button, labelresponse);

        Scene scene1 = new Scene(layout, 400, 250);
        primaryStage.setScene(scene1);

        primaryStage.show();
    }

    public Question DisplayQuestion(int ID) {
        for (Question q : questions) {
            if (q.ID == ID) {
                return q;
            }

        }
        return null;
    }

    public static void main(String[] args) {
        questions = FXCollections.observableArrayList(new Question("Cukor?", "Igen", "Nem", "Nem", "Nem"),
                new Question("Jó?", "Igen", "Nem", "Nem", "Nem"),
                new Question("Macciii?", "Igen", "Nem", "Nem", "nem"));
        launch(args);

    }

}
