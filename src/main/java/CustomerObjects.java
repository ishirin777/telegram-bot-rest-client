import lombok.Data;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


@Data
class CreditsAmount {

    private int totalCreditsAmountInAZN;
    private int totalCreditsAmountInUSD;

    String getTotalCreditsAmount(String lang) throws ParserConfigurationException, SAXException, IOException {
        LanguageElement languageElement = new LanguageElement(lang);
        return String.format(languageElement.creditsProperties.trim(), totalCreditsAmountInAZN, totalCreditsAmountInUSD);
    }
}

@Data
class CustomerAccounts {

    private int azn;
    private int usd;
    private int eur;

    String getAllInfo(String lang) throws ParserConfigurationException, SAXException, IOException {
        LanguageElement languageElement = new LanguageElement(lang);
        return String.format(languageElement.accountsProperties.trim(), azn, usd, eur);
    }
}

@Data
class CustomerNameAndSurname {

    private String customerName;
    private String customerSurname;
}







