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

public class XML {
    Document document;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    String FileName;

    public void XMLCreator(String FName) {
        App.logger.severe("Create the XML document");
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File("Questions.xml"));
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
                PrintWriter writer = new PrintWriter(FName, "UTF-8");           //ha nem létezik akkor létrehoz egy fájlt
                writer.print("<data></data>\n");
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e3) {
                App.logger.severe("IO or Format failure");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("IO or Format failure");
                alert.setContentText("IO or Format failure");
                alert.showAndWait();

            }
        }
        try {                                                              //mégegyszer megkísérli megnyitni a fájlt, most már jónak kell lennie
            document = builder.parse(new File(FName));
        } catch (IOException | SAXException e4) {
            App.logger.severe("File not found");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Flie not found");
            alert.setContentText("Nem lehet megnyitni a fájlt");
            alert.showAndWait();


        }
        document.getDocumentElement().normalize();
        this.FileName = FName;
    }

    /*public void SaveToXML(Question q);
    public ObservableList<Question> getQuestions ;*/
}
