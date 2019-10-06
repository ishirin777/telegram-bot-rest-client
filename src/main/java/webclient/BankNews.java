package webclient;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankNews {

    public static String getBankNews(String lang) throws IOException {

        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        ArrayList<String> fromToDateList = new ArrayList<>();

        if (lang.equals("az")) {
            HtmlPage page = webClient.getPage("https://www.pashabank.az/press_centre/lang,az/");
            List<HtmlDivision> listDate = page.getByXPath("//div[@class='news-box-news']");
            for (int i = 0; i < 3; ++i) {
                fromToDateList.add(listDate.get(i).asText());
            }

        }

        else if(lang.equals("en")){
            HtmlPage page = webClient.getPage("https://www.pashabank.az/press_centre/lang,en/");
            List<HtmlDivision> listDate = page.getByXPath("//div[@class='news-box-news']");
            for (int i = 0; i < 3; ++i) {
                fromToDateList.add(listDate.get(i).asText());
            }
        }

        else{
            HtmlPage page = webClient.getPage("https://www.pashabank.az/press_centre/lang,ru/");
            List<HtmlDivision> listDate = page.getByXPath("//div[@class='news-box-news']");
            for (int i = 0; i < 3; ++i) {
                fromToDateList.add(listDate.get(i).asText());
            }
        }

        return String.valueOf(fromToDateList);
    }
}




