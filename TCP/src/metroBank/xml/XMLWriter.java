package metroBank.xml;

import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import metroBank.stores.MetroCard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLWriter {

    private ArrayList<MetroCard> cards;

    public XMLWriter(ArrayList<MetroCard> c) {

        cards = c;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("MetroCardBank");
            doc.appendChild(rootElement);
            for (MetroCard card : cards)
                rootElement.appendChild(getNode(doc, card));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            transformer.transform(source, new StreamResult(new FileOutputStream(
                    "MetroCardBank.xml")));
            System.out.println("Файл створено");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node getNode(Document doc, MetroCard c) {
        Element node = doc.createElement("MetroCard");
        Element id = doc.createElement("id");
        Element user = doc.createElement("user");
        Element surname=doc.createElement("surname");
        Element name=doc.createElement("name");
        Element sex=doc.createElement("sex");
        Element bday=doc.createElement("birthday");
        Element college = doc.createElement("college");
        Element balance = doc.createElement("balance");

        id.appendChild(doc.createTextNode(c.getId()));
        college.appendChild(doc.createTextNode(c.getCollege()));
        balance.appendChild(doc.createTextNode(String.valueOf(c.getBalance())));

        surname.appendChild(doc.createTextNode(c.getUser().getSurName()));
        name.appendChild(doc.createTextNode(c.getUser().getName()));
        sex.appendChild(doc.createTextNode(c.getUser().getSex()));
        bday.appendChild(doc.createTextNode(c.getUser().getStrBirthday()));

        user.appendChild(surname);
        user.appendChild(name);
        user.appendChild(sex);
        user.appendChild(bday);

        node.appendChild(id);
        node.appendChild(user);
        node.appendChild(college);
        node.appendChild(balance);
        return node;
    }

}