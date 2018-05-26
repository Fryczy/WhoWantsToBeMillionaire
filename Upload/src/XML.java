import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Az XML-ben tárolt kérdések és válaszok kezelésére szolgáló osztály
 */
public class XML {
    Document doc;
    DocumentBuilderFactory fact;
    DocumentBuilder builder;
    String name;

    /**
     * Az XML dokumentumot létrehozó metódus
     * @param Fname
     */
    public void XMLCreator(String Fname) {
        App.logger.severe("Create the XML");
        try {
            fact = DocumentBuilderFactory.newInstance();
            builder = fact.newDocumentBuilder();
            doc = builder.parse(new File("Questions.xml"));
        } catch (ParserConfigurationException e) {
            App.logger.severe("XML building failure");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("XML building failure");
            alert.setContentText("XML building failure");
            alert.showAndWait();
        } catch (SAXException e1) {
            App.logger.severe("XML parsing failed");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("XML parsing failed");
            alert.setContentText("XML parsing failed");
            alert.showAndWait();
        } catch (IOException e1) {
            try {
                PrintWriter writer = new PrintWriter(Fname, "UTF-8");           //ha nem létezik akkor létrehoz egy fájlt
                writer.print("<firsttag></firsttag>\n");
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e3) {
                App.logger.severe("IO or Format failure");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(" Wrong IO or Format");
                alert.setContentText(" Wrong IO or Format");
                alert.showAndWait();

            }
        }
        try {                                                              //mégegyszer megkísérli megnyitni a fájlt, most már jónak kell lennie
            doc = builder.parse(new File(Fname));
        } catch (IOException | SAXException e4) {
            App.logger.severe("File not found");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Flie not found");
            alert.setContentText("File not found");
            alert.showAndWait();


        }
        doc.getDocumentElement().normalize();
        this.name = Fname;
    }

    /**
     * Beleírja az DOMba  a kérdéseket és a válaszokat
     * @param q
     */
    public void WriteDOM(Question q) {
        App.logger.severe("write DOM");

        try {
            Element question = doc.createElement("q");
            doc.getFirstChild().appendChild(question);     //a lista mindegy egyes elemét ez a tág zárja közre

            Element col1 = doc.createElement("ID");
            col1.appendChild(doc.createTextNode(q.GetID().toString()));
            Element col2 = doc.createElement("quest");
            col2.appendChild(doc.createTextNode(q.GetQuestion()));
            Element col3 = doc.createElement("true");
            col3.appendChild(doc.createTextNode(q.Getanswer().GetTrue()));
            Element col4 = doc.createElement("false1");
            col4.appendChild(doc.createTextNode(q.Getanswer().GetF1()));
            Element col5 = doc.createElement("false2");
            col5.appendChild(doc.createTextNode(q.Getanswer().GetF2()));
            Element col6 = doc.createElement("false3");
            col6.appendChild(doc.createTextNode(q.Getanswer().GetF3()));

            question.appendChild(col1);
            question.appendChild(col2);
            question.appendChild(col3);
            question.appendChild(col4);
            question.appendChild(col5);
            question.appendChild(col6);


        }
        catch(NullPointerException e)
        {
            App.logger.severe("No data to XML");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Empty input");
            alert.setContentText("empty question list");
            alert.showAndWait();
            App.mainwindow.close();
        }
    }

    /**
     * Elmenti az XML fájlba a DOM-ot
     */
    public void SaveToDOM() {
        App.logger.severe("Save DOM");
        TransformerFactory transfactory;
        Transformer trans;
        DOMSource source = new DOMSource(doc);
        StreamResult res = new StreamResult(new File(this.name));
        transfactory =  TransformerFactory.newInstance();

        try {
            trans = transfactory.newTransformer();
            try {
                trans.transform(source, res);
            } catch (TransformerException e) {
                App.logger.severe("Transfromer failure");
            }
        } catch (TransformerConfigurationException e) {
            App.logger.severe("Transfromer failure");
        }

    }


    public ObservableList<Question> GetAllQuestions()
    {
        App.logger.severe("Read DOM");

        ObservableList<Question> quests = FXCollections.observableArrayList();
        try {
            NodeList nodes = doc.getElementsByTagName("q");          //tagek lekérése egy elemen belül
            NodeList children;

            for (int i = 0; i < nodes.getLength(); i++) {

                children = nodes.item(i).getChildNodes();
                Question q = new Question();
                q.SetID(Integer.parseInt(children.item(0).getFirstChild().getTextContent()));
                q.SetQuestion(children.item(1).getFirstChild().getTextContent());
                q.Getanswer().SetTrue(children.item(2).getFirstChild().getTextContent());
                q.Getanswer().SetF1(children.item(3).getFirstChild().getTextContent());
                q.Getanswer().SetF2(children.item(4).getFirstChild().getTextContent());
                q.Getanswer().SetF3(children.item(5).getFirstChild().getTextContent());

                quests.add(q);
            }
        }
        catch (NullPointerException e) {
            App.logger.severe("Reading XML Failure");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reading XML Failure");
            alert.setContentText("Reading XML Failure");
            alert.showAndWait();
            App.mainwindow.close();
        }

        for (Question x:quests) {
            if (x.ID > Question.counter) {
                Question.counter = x.ID + 1;
            }
        }
        return quests;

    }

    /**
     * Mielőtt írunk a fájlba kitöröl mindent
     */
    public void ClearXMLFile(){

        Node node=doc.getFirstChild();
        while (node.hasChildNodes())
            node.removeChild(node.getFirstChild());
    }
}
