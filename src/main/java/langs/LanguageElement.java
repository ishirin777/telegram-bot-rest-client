package langs;

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

public class LanguageElement {

    public String startText = "Paşa Bank bot-a xoş gəlmişsiniz!\nDavam etmək üçün müvafiq dili seçin. \uD83D\uDE0A";
    public String sharePhoneNumberText;
    public String authenticationText;
    public String accountsViewNameText;
    public String creditsViewNameText;
    public String welcomeText;
    public String accountsPropertiesAZNText;
    public String accountsPropertiesUSDText;
    public String accountsPropertiesEURText;
    public String creditsPropertiesAZNText;
    public String creditsPropertiesUSDText;
    public String noUserInformationAvailableText;
    public String accountsTypesText;
    public String creditsTypesText;
    public String currencyRatesText;
    public String currencyRates;
    public String generalSettings;
    public String languageText;
    public String contactTheBank;
    public String selectAction;
    public String backFunction;
    public String chooseTheLanguage;
    public String bankPhoneNumbers;

    public LanguageElement(String lang) {
        try {
            sharePhoneNumberText = getValue("SharePhoneNumberText", xmlParser(lang));
            authenticationText = getValue("AuthenticationText", xmlParser(lang));
            accountsViewNameText = getValue("AccountsViewNameText", xmlParser(lang));
            creditsViewNameText = getValue("CreditsViewNameText", xmlParser(lang));
            welcomeText = getValue("WelcomeText", xmlParser(lang));
            accountsPropertiesAZNText = getValue("AccountsPropertiesAZNText", xmlParser(lang));
            accountsPropertiesUSDText = getValue("AccountsPropertiesUSDText", xmlParser(lang));
            accountsPropertiesEURText = getValue("AccountsPropertiesEURText", xmlParser(lang));
            creditsPropertiesAZNText = getValue("CreditsPropertiesAZNText", xmlParser(lang));
            creditsPropertiesUSDText = getValue("CreditsPropertiesUSDText", xmlParser(lang));
            noUserInformationAvailableText = getValue("NoUserInformationAvailableText", xmlParser(lang));
            accountsTypesText = getValue("AccountsTypesText", xmlParser(lang));
            creditsTypesText = getValue("CreditsTypesText", xmlParser(lang));
            currencyRatesText = getValue("CurrencyRatesText", xmlParser(lang));
            generalSettings = getValue("GeneralSettings", xmlParser(lang));
            currencyRates = getValue("CurrencyRates", xmlParser(lang));
            languageText = getValue("LanguageText", xmlParser(lang));
            contactTheBank = getValue("ContactTheBank", xmlParser(lang));
            selectAction = getValue("SelectAction", xmlParser(lang));
            backFunction = getValue("BackFunction", xmlParser(lang));
            chooseTheLanguage = getValue("ChooseTheLanguage", xmlParser(lang));
            bankPhoneNumbers = getValue("BankPhoneNumbers", xmlParser(lang));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
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