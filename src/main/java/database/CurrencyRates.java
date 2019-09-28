package database;

import langs.LanguageElement;
import webclient.CurrencyRatesContent;

public class CurrencyRates {

    private static final CurrencyRatesContent currencyRatesContent = new CurrencyRatesContent();

    private static final String USD_Buy = currencyRatesContent.USD_Buy;
    private static final String USD_Sell = currencyRatesContent.USD_Sell;
    private static final String EUR_Buy = currencyRatesContent.EUR_Buy;
    private static final String EUR_Sell = currencyRatesContent.EUR_Sell;
    private static final String GBP_Buy = currencyRatesContent.GBP_Buy;
    private static final String GBP_Sell = currencyRatesContent.GBP_Sell;
    private static final String RUB_Buy = currencyRatesContent.RUB_Buy;
    private static final String RUB_Sell = currencyRatesContent.RUB_Sell;
    private static final String TRY_Buy = currencyRatesContent.TRY_Buy;
    private static final String TRY_Sell = currencyRatesContent.TRY_Sell;

    public static String getCurrencyRates(String lang){
        LanguageElement languageElement = new LanguageElement(lang);
        return String.format(languageElement.currencyRates.trim(),USD_Buy,USD_Sell,EUR_Buy,EUR_Sell,GBP_Buy,GBP_Sell,RUB_Buy,
                RUB_Sell,TRY_Buy,TRY_Sell);
    }
}
