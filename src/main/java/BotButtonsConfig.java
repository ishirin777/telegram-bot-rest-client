import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BotButtonsConfig {

    void setButtons(SendMessage sendMessage, boolean lanSelected, String lang, String phoneNum) throws ParserConfigurationException, SAXException, IOException {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup()
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        if (!lanSelected) {
            keyboardFirstRow.add(new KeyboardButton("\ud83c\uddec\ud83c\udde7 English"));
            keyboardFirstRow.add(new KeyboardButton("\ud83c\udde6\ud83c\uddff Azərbaycan dili"));
            keyboardFirstRow.add(new KeyboardButton("\ud83c\uddf7\ud83c\uddfa Русский"));
            keyboardRowList.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboardRowList);
            return;
        }

        LanguageElement languageElement = new LanguageElement(lang);
        if (phoneNum == null) {
            KeyboardButton shareContactButton = new KeyboardButton().setText("\uD83D\uDCF1 " + languageElement.phone.trim())
                    .setRequestContact(true);
            keyboardFirstRow.add(shareContactButton);
        } else {
            keyboardFirstRow.add(new KeyboardButton("💵 " + languageElement.accountsViewName.trim()));
            keyboardFirstRow.add(new KeyboardButton("💳 " + languageElement.creditsViewName.trim()));
        }

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }


}
