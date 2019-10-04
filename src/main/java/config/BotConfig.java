package config;

import api.ApiRequest;
import api.OkHttpGet;
import auth.SessionPhoneNumber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.CurrencyRates;
import database.CustomerAccounts;
import database.CustomerCreditsAmount;
import database.CustomerInfo;
import langs.LanguageElement;
import langs.LanguageKey;
import langs.LanguageValue;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
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
import java.util.concurrent.atomic.AtomicReference;


public class BotConfig extends TelegramLongPollingSessionBot {

    private final OkHttpGet okHttpGet = new OkHttpGet();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();
    private final LanguageKey languageKey = new LanguageKey("lang");
    private LanguageValue langType;
    private final LanguageKey phoneStoreLanguageKey = new LanguageKey("phoneKey");

    public enum ButtonsType {
        NULL,
        ACCOUNTS,
        CREDITS,
        SETTINGS,
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
                    session.ifPresent(value -> value.setAttribute(languageKey, new LanguageValue("az")));
                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.authenticationText, true, "az", null, ButtonsType.NULL);
                    break;

                case "\ud83c\uddec\ud83c\udde7 English":
                    session.ifPresent(value -> value.setAttribute(languageKey, new LanguageValue("en")));
                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.authenticationText, true, "en", null, ButtonsType.NULL);
                    break;

                case "\ud83c\uddf7\ud83c\uddfa Русский":
                    session.ifPresent(value -> value.setAttribute(languageKey, new LanguageValue("ru")));
                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));

                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.authenticationText, true, "ru", null, ButtonsType.NULL);
                    break;

                case "/start":
                    var sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                    try {
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        if (sessionPhoneNumber.get().getPhoneNumber() != null) {
                            sessionPhoneNumber.get().setPhoneNumber(null);
                        }
                    } catch (NullPointerException ignored) {
                    }

                    langElements = new LanguageElement("az");
                    sendChatAction(update);
                    sendMsg(message, langElements.startText, false, null, null, ButtonsType.NULL);
                    break;

                case "\uD83D\uDCB8 Hesablarım":
                case "\uD83D\uDCB8 My Accounts":
                case "\uD83D\uDCB8 Мои Счета":

                    sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.accountsTypesText, true, langType.getLangType(), sessionPhoneNumber.get().getPhoneNumber(), ButtonsType.ACCOUNTS);
                    break;

                case "\uD83D\uDCBC Kreditlərim":
                case "\uD83D\uDCBC My Credits":
                case "\uD83D\uDCBC Мои Кредиты":

                    sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.creditsTypesText.trim(), true, langType.getLangType(), sessionPhoneNumber.get().getPhoneNumber(), ButtonsType.CREDITS);
                    break;

                case "\uD83D\uDCC8 Valyuta məzənnələri":
                case "\uD83D\uDCC8 Currency rates":
                case "\uD83D\uDCC8 Курсы валют":

                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    sendChatAction(update);
                    sendMsg(message, CurrencyRates.getCurrencyRates(langType.getLangType()), true, langType.getLangType(), null, ButtonsType.NULL);
                    break;

                case "\u2699 Tənzimləmələr":
                case "\u2699 Settings":
                case "\u2699 Настройки":

                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.selectAction, true, langType.getLangType(), null, ButtonsType.SETTINGS);
                    break;

                case "\uD83C\uDF0D Dil":
                case "\uD83C\uDF0D Language":
                case "\uD83C\uDF0D Язык":

                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.chooseTheLanguage, false, langType.getLangType(), null, ButtonsType.NULL);
                    break;

                case "\uD83D\uDCDE Bankla əlaqə":
                case "\uD83D\uDCDE Contact the bank":
                case "\uD83D\uDCDE Связаться с банком":

                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.bankPhoneNumbers.trim(), true, langType.getLangType(), null, ButtonsType.SETTINGS);
                    break;

                case "\uD83D\uDD19 Geri":
                case "\uD83D\uDD19 Back":
                case "\uD83D\uDD19 Назад":

                    session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                    langElements = new LanguageElement(langType.getLangType());
                    sendChatAction(update);
                    sendMsg(message, langElements.backFunction.trim(), true, langType.getLangType(), null, ButtonsType.NULL);
                    break;

                default:
                    sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                    try {
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));
                        sendChatAction(update);

                        if (langType == null) {
                            langElements = new LanguageElement("az");
                            sendMsg(message, langElements.selectAction, false, "az", null, ButtonsType.NULL);
                        } else if (langType.getLangType() != null) {
                            langElements = new LanguageElement(langType.getLangType());
                            sendMsg(message, langElements.selectAction, true, langType.getLangType(), null, ButtonsType.NULL);

                        } else if (sessionPhoneNumber.get().getPhoneNumber() == null) {
                            langElements = new LanguageElement(langType.getLangType());
                            sendMsg(message, langElements.selectAction, false, langType.getLangType(), null, ButtonsType.NULL);

                        } else {
                            langElements = new LanguageElement(langType.getLangType());
                            sendMsg(message, langElements.selectAction, true, langType.getLangType(), sessionPhoneNumber.get().getPhoneNumber(), ButtonsType.NULL);
                        }
                    } catch (NullPointerException ignored) {
                    }
                    break;
            }
        }
        else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText callbackMessage;
            AtomicReference<SessionPhoneNumber> sessionPhoneNumber;

            switch (call_data) {
                case "AZN_Account":
                    try {
                        sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));

                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber.get());
                        callbackMessage = CallBackResponse.editMessageText(chat_id, message_id, answer.getAZNAccountsFromDB(langType.getLangType()));
                        execute(callbackMessage);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "USD_Account":
                    try {
                        sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));

                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber.get());
                        callbackMessage = CallBackResponse.editMessageText(chat_id, message_id, answer.getUSDAccountsFromDB(langType.getLangType()));
                        execute(callbackMessage);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "EUR_Account":
                    try {
                        sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));

                        CustomerAccounts answer = CallBackResponse.customerAccountsDB(sessionPhoneNumber.get());
                        callbackMessage = CallBackResponse.editMessageText(chat_id, message_id, answer.getEURAccountsFromDB(langType.getLangType()));
                        execute(callbackMessage);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;

                case "AZN_Credits":
                    try {
                        sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));

                        CustomerCreditsAmount answer = CallBackResponse.customerCreditsAmountDB(sessionPhoneNumber.get());
                        callbackMessage = CallBackResponse.editMessageText(chat_id, message_id, answer.getTotalCreditsAmountInAZN(langType.getLangType()));
                        execute(callbackMessage);
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "USD_Credits":
                    try {
                        sessionPhoneNumber = new AtomicReference<>(new SessionPhoneNumber());
                        session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));
                        session.ifPresent(value -> sessionPhoneNumber.set((SessionPhoneNumber) value.getAttribute(phoneStoreLanguageKey)));

                        CustomerCreditsAmount answer = CallBackResponse.customerCreditsAmountDB(sessionPhoneNumber.get());
                        callbackMessage = CallBackResponse.editMessageText(chat_id, message_id, answer.getTotalCreditsAmountInUSD(langType.getLangType()));
                        execute(callbackMessage);
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

            var sessionPhoneNumber = new SessionPhoneNumber();
            sessionPhoneNumber.setPhoneNumber(customerPhoneNumber);
            session.ifPresent(value -> value.setAttribute(phoneStoreLanguageKey, sessionPhoneNumber));
            session.ifPresent(value -> langType = (LanguageValue) value.getAttribute(languageKey));

            langElements = new LanguageElement(langType.getLangType());
            sendChatAction(update);
            try {
                if (okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber)).isEmpty()) {
                    if (sessionPhoneNumber.getPhoneNumber() != null) {
                        sessionPhoneNumber.setPhoneNumber(null);
                    }
                    sendMsg(Objects.requireNonNull(message), langElements.noUserInformationAvailableText, false, null, null, ButtonsType.NULL);
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

    private void sendChatAction(Update update) {
        SendChatAction sendChatAction = new SendChatAction();
        try {
            sendChatAction.setChatId(update.getMessage().getChatId());
            sendChatAction.setAction(ActionType.TYPING);
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }
    }
}