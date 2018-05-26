import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class Answer {
    /**
     * A kérdésekhez tartozó válaszokat tartalmazza, mind a 4 válasz lehetőséget külön kell megadni úgy, hogy pontosan egy jó válasz lehet csak
     */
    String TrueAnswer;
    String False1;
    String False2;
    String False3;

    public Answer(){}

    public Answer(String TrueAnswer, String False1, String False2, String False3) {
        this.TrueAnswer=TrueAnswer;
        this.False1= False1;
        this.False2= False2;
        this.False3=False3;
    }

    public String GetTrue()
    {return this.TrueAnswer;}
    public void SetTrue(String t)
    {this.TrueAnswer=t;}

    public String GetF1()
    {return this.False1;}
    public void SetF1(String t)
    {this.False1=t;}

    public String GetF2()
    {return this.False2;}
    public void SetF2(String t)
    {this.False2=t;}

    public String GetF3()
    {return this.False3;}
    public void SetF3(String t)
    {this.False3=t;}

}
