package ru.extreme.bot.clickbot.clickclient;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.extreme.bot.clickbot.exception.ServiceException;

import static ru.extreme.bot.clickbot.exception.ExceptionVariables.CLIENT_EXCEPTION;

@Component
@RequiredArgsConstructor
public class ClickClient {

    @Value("${click.currency.url}")
    private String url;

    private final OkHttpClient client;

    public String getJsonProfileInstance(String endPoint, String token) throws ServiceException {
        Request request = new Request.Builder()
                    .get()
                    .url(url + endPoint)
                    .addHeader("accept", "application/json")
                    .addHeader("X-Auth-Token", token)
                    .build();

        return sendRequest(request);
    }

    public String getJsonAccountsInstance(String endPoint, String token, Long profileId) throws ServiceException {
        Request request = new Request.Builder()
                    .get()
                    .url(url + endPoint)
                    .addHeader("accept", "application/json")
                    .addHeader("X-Auth-Token", token)
                    .addHeader("X-Auth-UserId", String.valueOf(profileId))
                    .build();

        return sendRequest(request);
    }

    private String sendRequest(Request request) throws ServiceException {
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        } catch (Exception e) {
            throw new ServiceException(CLIENT_EXCEPTION.getCode(), e);
        }
    }

}
