package starter;

import config.BotConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {

    public static final String BOT_USERNAME = "DemoBankingBot";
    public static final String BOT_TOKEN = "789048924:AAGfWQLNkUPn6Hdbu5HjvxmA_yy-NqyF868";

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
