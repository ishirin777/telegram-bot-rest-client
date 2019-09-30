package config;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BotInlineButtonsConfig {

    private InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    private List<InlineKeyboardButton> rowInline = new ArrayList<>();

    void inlineKeyboardForCustomerAccounts(SendMessage sendMessage) {
        rowInline.add(new InlineKeyboardButton().setText("AZN").setCallbackData("AZN_Account"));
        rowInline.add(new InlineKeyboardButton().setText("USD").setCallbackData("USD_Account"));
        rowInline.add(new InlineKeyboardButton().setText("EUR").setCallbackData("EUR_Account"));
        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);
    }

    void inlineKeyboardForCustomerCredits(SendMessage sendMessage) {
        rowInline.add(new InlineKeyboardButton().setText("AZN").setCallbackData("AZN_Credits"));
        rowInline.add(new InlineKeyboardButton().setText("USD").setCallbackData("USD_Credits"));
        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);
    }

}
