package config;

import api.ApiRequest;
import api.OkHttpGet;
import auth.SessionPhoneNumber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.CustomerAccounts;
import database.CustomerCreditsAmount;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

class CallBackResponse {

    private final OkHttpGet okHttpGet = new OkHttpGet();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final Gson gson = gsonBuilder.create();

      EditMessageText editMessageText(long chat_id, long message_id, String answer) {
        EditMessageText editMessageText;
        editMessageText = new EditMessageText();
        editMessageText.setChatId(chat_id);
        editMessageText.setMessageId(Math.toIntExact(message_id));
        editMessageText.setText(answer);
        return editMessageText;
    }

      CustomerAccounts customerAccountsDB(SessionPhoneNumber sessionPhoneNumber) throws IOException {
        String response = okHttpGet.run(ApiRequest.getCustomerAccounts(sessionPhoneNumber.getPhoneNumber()));
        return gson.fromJson(response, CustomerAccounts[].class)[0];
    }

      CustomerCreditsAmount customerCreditsAmountDB(SessionPhoneNumber sessionPhoneNumber) throws IOException {
        String response = okHttpGet.run(ApiRequest.getTotalCustomerCreditsAmount(sessionPhoneNumber.getPhoneNumber()));
        return gson.fromJson(response, CustomerCreditsAmount[].class)[0];
    }

}
