package config;

import langs.LanguageElement;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BotInlineButtonsConfig extends InlineKeyboardMarkup {

    private InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    private List<InlineKeyboardButton> rowInline = new ArrayList<>();
    private List<List<InlineKeyboardButton>> rowList = new ArrayList<>();


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

    void inlineKeyboardForBankBranches(SendMessage sendMessage, LanguageElement languageElement) {
        rowInline.add(new InlineKeyboardButton().setText(languageElement.bankBakuBranchesViewNameText).setCallbackData("Baku_Branches"));
        rowInline.add(new InlineKeyboardButton().setText(languageElement.bankRegionalBranchesViewNameText).setCallbackData("Regional_Branches"));
        markupInline.setKeyboard(Collections.singletonList(rowInline));
        sendMessage.setReplyMarkup(markupInline);
    }

    EditMessageText inlineKeyboardForBakuBranch(EditMessageText editMessageText) {
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Baş Ofis").setCallbackData("Head_Office")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Fərdi Bankçılıq şöbəsi").setCallbackData("Individual_Banking")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Bridge Plaza şöbəsi").setCallbackData("Bridge_Plaza")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Port Baku şöbəsi").setCallbackData("Port_Baku")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Landmark şöbəsi").setCallbackData("Landmark")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Şüvəlan şöbəsi").setCallbackData("Shuvalan")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("28 May mübadilə şöbəsi").setCallbackData("28_May")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Ağ şəhər mübadilə şöbəsi").setCallbackData("White_City")));
        markupInline.setKeyboard(rowList);
        return editMessageText.setReplyMarkup(markupInline);
    }

    EditMessageText inlineKeyboardForRegionalBranches(EditMessageText editMessageText) {
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Gəncə filialı").setCallbackData("Ganja_Branch")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Zaqatala filialı").setCallbackData("Zagatala_Branch")));
        rowList.add(Collections.singletonList(new InlineKeyboardButton().setText("Quba filialı").setCallbackData("Guba_Branch")));
        markupInline.setKeyboard(rowList);
        return editMessageText.setReplyMarkup(markupInline);
    }
}

