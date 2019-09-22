import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

class LanguageElement {

    String phone;
    String accountsViewName;
    String creditsViewName;
    String welcomeText;
    String accountsProperties;
    String creditsProperties;
    String noResultsResponse;
    String noUserInformationAvailable;

    LanguageElement(String lang) throws IOException, SAXException, ParserConfigurationException {
        this.phone = getValue("PhoneNumber", xmlParser(lang));
        this.accountsViewName = getValue("AccountsViewName", xmlParser(lang));
        this.creditsViewName = getValue("CreditsViewName", xmlParser(lang));
        this.welcomeText = getValue("WelcomeText", xmlParser(lang));
        this.accountsProperties = getValue("AccountsProperties", xmlParser(lang));
        this.creditsProperties = getValue("CreditsProperties", xmlParser(lang));
        this.noResultsResponse = getValue("NoResultsResponse", xmlParser(lang));
        this.noUserInformationAvailable = getValue("NoUserInformationAvailable", xmlParser(lang));
    }

    private Element xmlParser(String language) throws ParserConfigurationException, IOException, SAXException {
        File file = new File("src/main/resources/languages.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        return (Element) doc.getElementsByTagName(language).item(0);
    }

    private static String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodes.item(0);
        return node.getNodeValue();
    }
}