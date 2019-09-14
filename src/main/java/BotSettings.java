import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BotSettings extends TelegramLongPollingBot {

    private final OkHttpGet okHttpGet = new OkHttpGet();
    private final OkHttpPost okHttpPost = new OkHttpPost();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();

    public void sendMsg(Message message, String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        setButtons(sendMessage, chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String msg = message.getText();
            switch (msg) {
                case "/start":
                    try {
                        if (!okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId)).isEmpty()) {
                            String phoneNmbJSON = okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId));
                            JSONObject json = new JSONObject(phoneNmbJSON);
                            String phoneNmb = json.getString("customerPhoneNumber");

                            String response = okHttpGet.run(ApiRequest.getCustomerNameAndSurname(phoneNmb));
                            CustomerNameAndSurname customerNameAndSurname = gson.fromJson(response, CustomerNameAndSurname[].class)[0];
                            sendMsg(message, "Salam, " + customerNameAndSurname.getCustomerName() + " "
                                    + customerNameAndSurname.getCustomerSurname()
                                    + "." + " Siz artıq sistemə daxil oldunuz. Aşağıdakı menyudan öz seçimlərinizi edə bilərsiniz.", chatId);
                        } else {
                            sendMsg(message, "Salam, hörmətli müştəri! Sistemə daxil olmaq üçün zəhmət olmasa nömrənizi paylaşın.", chatId);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Hesablar":
                    try {
                        String responseForCustomerPhoneNumber = okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId));
                        JSONObject json = new JSONObject(responseForCustomerPhoneNumber);
                        String customerPhoneNumberByChatId = json.getString("customerPhoneNumber");

                        String response = okHttpGet.run(ApiRequest.getCustomerAccounts(customerPhoneNumberByChatId));
                        CustomerAccounts customerAccounts = gson.fromJson(response, CustomerAccounts[].class)[0];
                        sendMsg(message, customerAccounts.getAllInfo(), chatId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Kreditlər":
                    try {
                        String responseForCustomerPhoneNumber = okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId));
                        JSONObject json = new JSONObject(responseForCustomerPhoneNumber);
                        String customerPhoneNumberByChatId = json.getString("customerPhoneNumber");

                        String response = okHttpGet.run(ApiRequest.getTotalCustomerCreditsAmount(customerPhoneNumberByChatId));
                        CreditsAmount creditsAmount = gson.fromJson(response, CreditsAmount[].class)[0];
                        sendMsg(message, String.valueOf(creditsAmount.getTotalCreditsAmount()), chatId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMsg(message, "Sorğunuz üzrə məlumat tapılmadı.", chatId);
                    break;
            }
        } else if (update.getMessage().getContact() != null) {
            String customerPhoneNumber = update.getMessage().getContact().getPhoneNumber();
            if (customerPhoneNumber.contains("+")) {
                customerPhoneNumber = customerPhoneNumber.substring(1);
            }
            try {
                if (okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber)).isEmpty()) {
                    sendMsg(message, "Sizin məlumat bankın bazasında yoxdur. Zəhmət olmasa bank ilə əlaqə saxlayın.", chatId);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId)).isEmpty()) {
                    okHttpPost.post(ApiRequest.postCustomerChatIdAndPhoneNumber(), chatId, customerPhoneNumber);
                }
                String response = okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber));
                CustomerNameAndSurname customerNameAndSurname = gson.fromJson(response, CustomerNameAndSurname[].class)[0];
                sendMsg(message, "Salam, " + customerNameAndSurname.getCustomerName() + " "
                        + customerNameAndSurname.getCustomerSurname()
                        + "." + " Siz artıq sistemə daxil oldunuz. Aşağıdakı menyudan öz seçimlərinizi edə bilərsiniz.", chatId);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setButtons(SendMessage sendMessage, Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        String responseForCustomerPhoneNumber = null;
        try {
            responseForCustomerPhoneNumber = okHttpGet.run(ApiRequest.getCustomerByCustomerChatId(chatId));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert responseForCustomerPhoneNumber != null;
        if (responseForCustomerPhoneNumber.isEmpty()) {
            KeyboardButton shareContactButton = new KeyboardButton();
            shareContactButton.setText("Nömrəmi paylaş").setRequestContact(true);
            keyboardFirstRow.add(shareContactButton);
        }

        if (!responseForCustomerPhoneNumber.isEmpty()) {
            keyboardFirstRow.add(new KeyboardButton("Hesablar"));
            keyboardFirstRow.add(new KeyboardButton("Kreditlər"));
        }

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "TestNewInternetBankingBot";
    }

    public String getBotToken() {
        return "870697719:AAGH_2OjkRGy-hdYEDYTkWuk0oUlBydfBsA";
    }

}

