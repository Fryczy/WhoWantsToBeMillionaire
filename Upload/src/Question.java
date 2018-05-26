import com.sun.javafx.text.TextLine;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * A kérdéseket és válaszokat tároló osztály
 *
 */
public class Question {
    String question;
    int ID;
    static int counter=0;
    Answer answers;


    public Question(){
        answers=new Answer();
        this.ID=counter;
        counter++;
    }
    public Question(String q,String T,String F1, String F2,String F3) {
            question=q;
            answers= new Answer(T, F1, F2,F3);
            ID = counter;
            counter++;

    }

    public Integer GetID()
    {
        return this.ID;
    }
    public void SetID(int v)
    {this.ID=v;}

    public String GetQuestion()
    {
        return question;
    }
    public void SetQuestion (String v)
    {this.question=v;}

    public Answer Getanswer(){return this.answers;}

    /**
     * Az Appban lévő questons listához hozzá ad egy kérdést és megalkotja egy kérdés hozzáadásához szükséges formot is
     * a létrehozás során ki kell tölteni minden mezőt és, csak a TruaAnswer mezőbe írt kérédés tekinit a rendszer helyesnek
     */
    static public void AddNewQuestion()
    {
        Stage newQ= new Stage();
        newQ.setTitle("New Question");
        Scene scene;

        GridPane grid =new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(40);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField quest=new TextField();
        Label questlabel =new Label("Question");

        TextField TrueAnswer=new TextField();
        Label Truelab =new Label("True answer");
        TextField false1=new TextField();
        Label flab1 =new Label("False answer");
        TextField false2=new TextField();
        Label flab2 =new Label("False answer");
        TextField false3=new TextField();
        Label flab3 =new Label("False answer");

        Button add=new Button("Add");
        add.setOnAction(e->{
            Question q=new Question();
            int ok=0;
            try{
                q.question=quest.getText();
                q.answers.TrueAnswer=TrueAnswer.getText();
                q.answers.False1=false1.getText();
                q.answers.False2=false2.getText();
                q.answers.False3=false3.getText();

                if(q.question.equals("") || q.answers.TrueAnswer.equals("") ||
                        q.answers.False1.equals("") || q.answers.False2.equals("") || q.answers.False3.equals(""))
                {throw new NullPointerException();}

            }

            catch(NullPointerException ex){
                ok=1;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Empty Fields");
                alert.setHeaderText("Warning!");
                alert.setContentText("Fill all fields!");
                alert.showAndWait();
            }
            if (ok==0)
            {
                App.questions.add(q);
                App.UpdateXML(App.xml);
                newQ.close();
            }


        });

        grid.add(questlabel,1,1);
        grid.add(Truelab,2,1);
        grid.add(flab1,2,3);
        grid.add(flab2,2,5);
        grid.add(flab3,2,7);

        grid.add(quest,1,2);
        grid.add(TrueAnswer,2,2);
        grid.add(false1,2,4);
        grid.add(false2,2,6);
        grid.add(false3,2,8);

        grid.add(add,2,10);


        scene=new Scene(grid, 600,500);

        newQ.setScene(scene);
        newQ.show();

    }
}
