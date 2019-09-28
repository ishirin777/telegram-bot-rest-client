package database;

import langs.LanguageElement;
import lombok.Data;

@Data
public class CustomerCreditsAmount {

    private int totalCreditsAmountInAZN;
    private int totalCreditsAmountInUSD;
    LanguageElement languageElement;

    public String getTotalCreditsAmountInAZN(String lang){
        languageElement = new LanguageElement(lang);
        return String.format(languageElement.creditsPropertiesAZNText.trim(), totalCreditsAmountInAZN);
    }

    public String getTotalCreditsAmountInUSD(String lang){
        languageElement = new LanguageElement(lang);
        return String.format(languageElement.creditsPropertiesUSDText.trim(), totalCreditsAmountInUSD);
    }
}
