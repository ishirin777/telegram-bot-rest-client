package config;

import api.ApiRequest;
import api.OkHttpGet;
import auth.SessionPhoneNumber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.CurrencyRates;
import database.CustomerCreditsAmount;
import database.CustomerInfo;
import langs.Key;
import langs.LanguageElement;
import langs.Value;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import starter.Main;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class BotConfig extends TelegramLongPollingSessionBot {

    private final OkHttpGet okHttpGet = new OkHttpGet();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    private final Key key = new Key("lang");
    private Value langType;
    private final Key phoneStoreKey = new Key("phoneKey");
    private SessionPhoneNumber sessionPhoneNumber = new SessionPhoneNumber();

    public enum ButtonsType {
        NULL,
        ACCOUNTS,
        CREDITS,
        SETTINGS
    }

    private void sendMsg(Message message, String text, boolean langSelected, String lang, String phoneNum, ButtonsType buttonsType) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage, langSelected, lang, phoneNum, buttonsType);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update, Optional<Session> session) {
        Message message = update.getMessage();
        LanguageElement langElements;
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "\ud83c\udde6\ud83c\uddff Azərbaycan dili":
                    session.ifPresent(value -> value.setAttribute(key, new Value("az")));
                    sendMsg(message, "Sistemə daxil olmaq üçün «nömrəmi paylaş» düyməsinə klikləyin.", true, "az", null, ButtonsType.NULL);
                    break;

                case "\ud83c\uddec\ud83c\udde7 English":
                    session.ifPresent(value -> value.setAttribute(key, new Value("en")));
                    sendMsg(message, "To access the system, click the «share my number» button below.", true, "en", null, ButtonsType.NULL);
                    break;

                case "\ud83c\uddf7\ud83c\uddfa Русский":
                    session.ifPresent(value -> value.setAttribute(key, new Value("ru")));
                    sendMsg(message, "Чтобы получить доступ к системе, нажмите кнопку «поделиться своим номером» ниже.", true, "ru", null, ButtonsType.NULL);
                    break;

                case "/start":
                    sendMsg(message, "Paşa Bank bot-a xoş gəlmisiniz!\n" + "Davam etmək üçün müvafiq dili seçin. \uD83D\uDE42", false, null, null, ButtonsType.NULL);
                    break;

                case "\uD83D\uDCB8 Hesablar":
                case "\uD83D\uDCB8 Accounts":
                case "\uD83D\uDCB8 Счеты":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.accountsTypesText, true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.ACCOUNTS);
                    break;

                case "\uD83D\uDCBC Kreditlər":
                case "\uD83D\uDCBC Credits":
                case "\uD83D\uDCBC Кредиты":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    System.out.println(sessionPhoneNumber.getPhoneNumber());


                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.creditsTypesText, true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.CREDITS);
                    break;

                case "\uD83D\uDCC8 Valyuta məzənnələri":
                case "\uD83D\uDCC8 Currency rates":
                case "\uD83D\uDCC8 Курсы валют":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    sendMsg(message, CurrencyRates.getCurrencyRates(langType.getLangType()), true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.NULL);
                    break;

                case "\u2699 Tənzimləmələr":
                case "\u2699 Settings":
                case "\u2699 Настройки":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.selectAction, true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.SETTINGS);
                    break;

                case "\uD83C\uDF0D Dil":
                case "\uD83C\uDF0D Language":
                case "\uD83C\uDF0D Язык":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.chooseTheLanguage, false, null, null, ButtonsType.NULL);
                    break;

                case "\uD83D\uDD19 Geri":
                case "\uD83D\uDD19 Back":
                case "\uD83D\uDD19 Назад":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.selectAction, true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.NULL);
                    break;

                case "\uD83D\uDCDE Bankla əlaqə":
                case "\uD83D\uDCDE Contact the bank":
                case "\uD83D\uDCDE Связаться с банком":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.bankPhoneNumbers.trim(), true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.SETTINGS);
                    break;

                default:
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
//                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendMsg(message, langElements.selectAction, true, langType.getLangType(), null, ButtonsType.NULL);
                    break;
            }
        }

       /* if (update.hasChannelPost()) {
            Message channelMessage = update.getChannelPost();
            String getChannelMessage = update.getChannelPost().getText();
            System.out.println(getChannelMessage);
            sendMsg(channelMessage, getChannelMessage, true, langType.getLangType(), sessionPhoneNumber.getPhoneNumber(), ButtonsType.NULL);
        } */

        else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText new_message;

            switch (call_data) {
                    /*  case "AZN_Account":
                    try {
                  session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));


                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber);
                        new_message = editMessageText(chat_id, message_id, answer.getAZNAccountsFromDB(langType.getLangType()));

                        execute(new_message);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                   */
                /* case "USD_Account":

                    try {

                        session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber);
                        new_message = editMessageText(chat_id, message_id, answer.getUSDAccountsFromDB(langType.getLangType()));
                        execute(new_message);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break; */

               /* case "EUR_Account":
                    try {
                        session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber);
                        new_message = editMessageText(chat_id, message_id, answer.getEURAccountsFromDB(langType.getLangType()));
                        execute(new_message);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break; */


                case "AZN_Credits":
                    try {

                        session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));


                        System.out.println(sessionPhoneNumber.getPhoneNumber());

                        CallBackResponse callBackResponse = new CallBackResponse();


                        CustomerCreditsAmount answer = callBackResponse.customerCreditsAmountDB(sessionPhoneNumber);
                        new_message = callBackResponse.editMessageText(chat_id, message_id, answer.getTotalCreditsAmountInAZN(langType.getLangType()));
                        execute(new_message);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "USD_Credits":
                    try {

                        session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                        session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));

                        System.out.println(sessionPhoneNumber.getPhoneNumber());

                        CallBackResponse callBackResponse = new CallBackResponse();


                        CustomerCreditsAmount answer = callBackResponse.customerCreditsAmountDB(sessionPhoneNumber);
                        new_message = callBackResponse.editMessageText(chat_id, message_id, answer.getTotalCreditsAmountInUSD(langType.getLangType()));
                        execute(new_message);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        } else if (Objects.requireNonNull(update.getMessage()).getContact() != null) {
            String customerPhoneNumber = update.getMessage().getContact().getPhoneNumber();
            customerPhoneNumber = customerPhoneNumber.contains("+") ? customerPhoneNumber.substring(1) : customerPhoneNumber;

            sessionPhoneNumber.setPhoneNumber(customerPhoneNumber);
            session.ifPresent(value -> value.setAttribute(phoneStoreKey, sessionPhoneNumber));
            session.ifPresent(value -> langType = (Value) value.getAttribute(key));
            langElements = new LanguageElement(langType.getLangType());
            try {
                if (okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber)).isEmpty()) {
                    sendMsg(Objects.requireNonNull(message), langElements.noUserInformationAvailableText, false, null, customerPhoneNumber, ButtonsType.NULL);
                    return;
                }

                String response = okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber));
                CustomerInfo customerNameAndSurname = gson.fromJson(response, CustomerInfo.class);
                sendMsg(Objects.requireNonNull(message), String.format(langElements.welcomeText.trim(),
                        customerNameAndSurname.getCustomerName(), customerNameAndSurname.getCustomerSurname())
                        , true, langType.getLangType(), customerPhoneNumber, ButtonsType.NULL);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setButtons(SendMessage sendMessage, boolean langSelected, String lang, String phoneNum, ButtonsType buttonsType) {
        BotButtonsConfig.setButtons(sendMessage, langSelected, lang, phoneNum, buttonsType);
    }

    public String getBotUsername() {
        return Main.BOT_USERNAME;
    }

    public String getBotToken() {
        return Main.BOT_TOKEN;
    }
}