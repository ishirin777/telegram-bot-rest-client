import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    static final String BOT_USERNAME = "TestNewInternetBankingBot";
    static final String BOT_TOKEN = "870697719:AAGH_2OjkRGy-hdYEDYTkWuk0oUlBydfBsA";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new BotConfig());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
