import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

    private void sendMsg(Message message, String text, boolean lanSelected, String lang, String phoneNum) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true).setChatId(message.getChatId().toString()).setText(text);
        try {
            setButtons(sendMessage, lanSelected, lang, phoneNum);
            execute(sendMessage);
        } catch (IOException | ParserConfigurationException | SAXException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update, Optional<Session> session) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "\ud83c\udde6\ud83c\uddff Azərbaycan dili":
                    session.ifPresent(value -> value.setAttribute(key, new Value("az")));
                    sendMsg(message, "İndisə «nömrəmi paylaş» düyməsinə klikləyin.", true, "az", null);
                    break;
                case "\ud83c\uddec\ud83c\udde7 English":
                    session.ifPresent(value -> value.setAttribute(key, new Value("en")));
                    sendMsg(message, "Now, click the «share my number» button below.", true, "en", null);
                    break;
                case "\ud83c\uddf7\ud83c\uddfa Русский":
                    session.ifPresent(value -> value.setAttribute(key, new Value("ru")));
                    sendMsg(message, "Теперь нажмите кнопку «поделись моим номером» ниже.", true, "ru", null);
                    break;
                case "/start":
                    sendMsg(message, "Paşa Bank bot-a xoş gəlmisiniz!\n" + "Davam etmək üçün müvafiq dili seçin.", false, null, null);
                    break;
                case "💵 Счеты":
                case "💵 Accounts":
                case "💵 Hesablar":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));
                    try {
                        String response = okHttpGet.run(ApiRequest.getCustomerAccounts(sessionPhoneNumber.getPhoneNumber()));
                        CustomerAccounts customerAccounts = gson.fromJson(response, CustomerAccounts[].class)[0];
                        sendMsg(message, customerAccounts.getAllInfo(langType.getLangType()), !langType.getLangType().isEmpty(), langType.getLangType(), sessionPhoneNumber.getPhoneNumber());

                    } catch (IOException | SAXException | ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "💳 Кредиты":
                case "💳 Credits":
                case "💳 Kreditlər":
                    session.ifPresent(value -> langType = (Value) value.getAttribute(key));
                    session.ifPresent(value -> sessionPhoneNumber = (SessionPhoneNumber) value.getAttribute(phoneStoreKey));
                    try {
                        String response = okHttpGet.run(ApiRequest.getTotalCustomerCreditsAmount(sessionPhoneNumber.getPhoneNumber()));
                        CreditsAmount creditsAmount = gson.fromJson(response, CreditsAmount[].class)[0];
                        sendMsg(message, String.valueOf(creditsAmount.getTotalCreditsAmount(langType.getLangType())), !langType.getLangType().isEmpty(), langType.getLangType(), sessionPhoneNumber.getPhoneNumber());
                    } catch (IOException | SAXException | ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        LanguageElement languageElement = new LanguageElement(langType.getLangType());
                        sendMsg(message, languageElement.noResultsResponse, !langType.getLangType().isEmpty(), langType.getLangType(), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (Objects.requireNonNull(update.getMessage()).getContact() != null) {
            String customerPhoneNumber = update.getMessage().getContact().getPhoneNumber();
            customerPhoneNumber = customerPhoneNumber.contains("+") ? customerPhoneNumber.substring(1) : customerPhoneNumber;

            sessionPhoneNumber.setPhoneNumber(customerPhoneNumber);
            session.ifPresent(value -> value.setAttribute(phoneStoreKey, sessionPhoneNumber));
            session.ifPresent(value -> langType = (Value) value.getAttribute(key));

            try {
                if (okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber)).isEmpty()) {
                    LanguageElement languageElement = new LanguageElement(langType.getLangType());
                    sendMsg(message, languageElement.noUserInformationAvailable, false, null, customerPhoneNumber);
                    return;
                }
                LanguageElement languageElement = new LanguageElement(langType.getLangType());
                String response = okHttpGet.run(ApiRequest.getCustomerNameAndSurname(customerPhoneNumber));
                CustomerNameAndSurname customerNameAndSurname = gson.fromJson(response, CustomerNameAndSurname.class);
                sendMsg(message, String.format(languageElement.welcomeText.trim(),
                        customerNameAndSurname.getCustomerName(), customerNameAndSurname.getCustomerSurname())
                        , true, langType.getLangType(), customerPhoneNumber);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setButtons(SendMessage sendMessage, boolean lanSelected, String lang, String phoneNum) throws IOException, ParserConfigurationException, SAXException {

        BotButtonsConfig botButtonsConfig = new BotButtonsConfig();
        botButtonsConfig.setButtons(sendMessage, lanSelected, lang, phoneNum);
    }

    public String getBotUsername() {
        return Main.BOT_USERNAME;
    }

    public String getBotToken() {
        return Main.BOT_TOKEN;
    }
}