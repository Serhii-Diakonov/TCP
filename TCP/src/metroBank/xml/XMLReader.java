package metroBank.xml;

import metroBank.stores.MetroCard;
import metroBank.stores.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLReader extends DefaultHandler {

    private boolean isID;
    private boolean isName;
    private boolean isSurname;
    private boolean isSex;
    private boolean isBirthday;
    private boolean isCollege;
    private boolean isBalance;

    private String ID;
    private String name;
    private String surname;
    private String sex;
    private String bday;
    private String college;
    private double balance;

    private ArrayList<MetroCard> cards;

    public XMLReader() {
        cards = new ArrayList<>();
        File f=new File("MetroCardBank.xml");
        SAXParserFactory factory=SAXParserFactory.newInstance();
        try{
            SAXParser parser=factory.newSAXParser();
            parser.parse(f, this);
        }catch (SAXException | ParserConfigurationException | IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<MetroCard> getCards() {
        return cards;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "id" -> isID = true;
            case "name" -> isName = true;
            case "surname" -> isSurname = true;
            case "sex" -> isSex = true;
            case "birthday" -> isBirthday = true;
            case "college" -> isCollege = true;
            case "balance" -> isBalance = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "id" -> isID = false;
            case "name" -> isName = false;
            case "surname" -> isSurname = false;
            case "sex" -> isSex = false;
            case "birthday" -> isBirthday = false;
            case "college" -> isCollege = false;
            case "balance" -> {
                cards.add(new MetroCard(ID, new User(name, surname, sex, bday), college, balance));
                isBalance = false;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isID)
            ID = new String(ch, start, length).trim();
        if (isName)
            name = new String(ch, start, length).trim();
        if (isSurname)
            surname = new String(ch, start, length).trim();
        if (isSex)
            sex = new String(ch, start, length).trim();
        if (isBirthday)
            bday = new String(ch, start, length).trim();
        if (isCollege)
            college = new String(ch, start, length).trim();
        if (isBalance)
            balance = Double.parseDouble(new String(ch, start, length).trim());
    }

    @Override
    public void endDocument() throws SAXException {
//        for (MetroCard d : cards) System.out.println(d.toString() + "\n");
    }
}