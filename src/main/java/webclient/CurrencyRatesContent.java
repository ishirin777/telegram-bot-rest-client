package webclient;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.io.IOException;
import java.util.Objects;

public class CurrencyRatesContent {

   private WebClient webClient = new WebClient();
   {
      webClient.getOptions().setJavaScriptEnabled(false);
      webClient.getOptions().setCssEnabled(false);
   }
    private HtmlPage page;

   {
      try {
         page = webClient.getPage("https://www.pashabank.az/exchange_valyuta_azn_currency_rate/lang,az/");

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private HtmlTable table = (HtmlTable) Objects.requireNonNull(page).getByXPath("//table[@class='currency_top currency_prices']").get(1);

    public String USD_Buy = table.getCellAt(1, 1).asText();
    public String USD_Sell = table.getCellAt(1, 2).asText();

    public String EUR_Buy = table.getCellAt(2, 1).asText();
    public String EUR_Sell = table.getCellAt(2, 2).asText();

    public String GBP_Buy = table.getCellAt(3, 1).asText();
    public String GBP_Sell = table.getCellAt(3, 2).asText();

    public String RUB_Buy = table.getCellAt(4, 1).asText();
    public String RUB_Sell = table.getCellAt(4, 2).asText();

    public String TRY_Buy = table.getCellAt(5, 1).asText();
    public String TRY_Sell = table.getCellAt(5, 2).asText();
}
