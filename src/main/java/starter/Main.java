package starter;

import config.BotConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static final String BOT_USERNAME = "DemoOnlineBankBot";
    public static final String BOT_TOKEN = "893987511:AAHdNMkGbRWN_wF5NcQtcNmcEP7x7lv4S0o";

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
