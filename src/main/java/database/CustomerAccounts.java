package database;

import langs.LanguageElement;
import lombok.Data;

@Data
public class CustomerAccounts {

    private int azn;
    private int usd;
    private int eur;
    private LanguageElement languageElement;

    public String getAZNAccountsFromDB(String lang) {
        languageElement = new LanguageElement(lang);
        return String.format((languageElement.accountsPropertiesAZNText).trim(), azn);
    }

    public String getUSDAccountsFromDB(String lang) {
        languageElement = new LanguageElement(lang);
        return String.format(languageElement.accountsPropertiesUSDText.trim(), usd);
    }

    public String getEURAccountsFromDB(String lang) {
        languageElement = new LanguageElement(lang);
        return String.format(languageElement.accountsPropertiesEURText.trim(), eur);
    }
}