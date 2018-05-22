import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Question {
    String question;
    int ID;
    static int counter=0;
    Answer answers;
    public Question(String q,String T,String F1, String F2,String F3) {
            question=q;
            answers= new Answer(T, F1, F2,F3);
            ID = counter;
            counter++;

    }
}
