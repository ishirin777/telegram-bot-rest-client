package config;

import langs.LanguageElement;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

class BotButtonsConfig{

    static void setButtons(SendMessage sendMessage, boolean langSelected, String lang, String phoneNum, BotConfig.MessageType MessageType){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup()
                .setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        if (!langSelected) {
            keyboardFirstRow.add(new KeyboardButton("\ud83c\udde6\ud83c\uddff Azərbaycan dili"));
            keyboardSecondRow.add(new KeyboardButton("\ud83c\uddec\ud83c\udde7 English"));
            keyboardThirdRow.add(new KeyboardButton("\ud83c\uddf7\ud83c\uddfa Русский"));
            keyboardRowList.add(keyboardFirstRow);
            keyboardRowList.add(keyboardSecondRow);
            keyboardRowList.add(keyboardThirdRow);
            replyKeyboardMarkup.setKeyboard(keyboardRowList);
            return;
        }
        LanguageElement languageElement = new LanguageElement(lang);
        if (phoneNum == null) {
            KeyboardButton shareContactButton = new KeyboardButton().setText("\uD83D\uDCF1 " + languageElement.sharePhoneNumberText.trim())
                    .setRequestContact(true);
            keyboardFirstRow.add(shareContactButton);


        } else {
            keyboardFirstRow.add(new KeyboardButton("\uD83D\uDCB8 " + languageElement.accountsViewNameText.trim()));
            keyboardFirstRow.add(new KeyboardButton("\uD83D\uDCB3 " + languageElement.creditsViewNameText.trim()));
            keyboardSecondRow.add(new KeyboardButton("\uD83D\uDCC8 " + languageElement.currencyRatesText.trim()));
            keyboardSecondRow.add(new KeyboardButton("\uD83D\uDCF0 " + languageElement.bankNewsText.trim()));
            keyboardRowList.add(keyboardSecondRow);

            BotInlineButtonsConfig botInlineButtonsConfig = new BotInlineButtonsConfig();

            switch (MessageType) {
                case ACCOUNTS:
                    botInlineButtonsConfig.inlineKeyboardForCustomerAccounts(sendMessage);
                    break;
                case CREDITS:
                    botInlineButtonsConfig.inlineKeyboardForCustomerCredits(sendMessage);
                    break;
                case CURRENCY_RATES:
                    botInlineButtonsConfig.inlineKeyboardForCurrencyRates(sendMessage);
                    break;
                default:
                    break;
            }
        }

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
