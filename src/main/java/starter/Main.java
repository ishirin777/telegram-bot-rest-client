package starter;

import config.BotConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static final String BOT_USERNAME = "DemoInternetBankingBot";
    public static final String BOT_TOKEN = "804230862:AAEwBq4vovgZP7Ee8N9Hu5pnvT5bPJ3nOOk";

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
