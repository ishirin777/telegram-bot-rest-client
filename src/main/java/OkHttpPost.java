import okhttp3.*;

import java.io.IOException;

public class OkHttpPost {

    OkHttpClient client = new OkHttpClient();

    String post(String url, Long chatId, String customerPhoneNumber) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("customerChatId", String.valueOf(chatId))
                .addFormDataPart("customerPhoneNumber", customerPhoneNumber)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


}
