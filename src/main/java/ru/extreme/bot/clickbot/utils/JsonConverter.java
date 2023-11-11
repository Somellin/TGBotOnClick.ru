package ru.extreme.bot.clickbot.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Класс для конвертации Json в сущности
 */
public final class JsonConverter {

    private final static Gson gson = new Gson();

    private JsonConverter() {
    }

    public static ClickProfile convertProfileJson(String jsonProfile) {
        JsonObject jsonObject = gson.fromJson(jsonProfile, JsonObject.class);
        JsonObject json = jsonObject.getAsJsonObject("response");

        return gson.fromJson(json, ClickProfile.class);
    }

    public static Collection<? extends ProfileAccount> convertAccountsJson(String jsonUsers) {
        JsonObject jsonObject = gson.fromJson(jsonUsers, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonObject("response").getAsJsonArray("accounts");

        Type listType = new TypeToken<List<ProfileAccount>>() {}.getType();

        return gson.fromJson(jsonArray, listType);
    }
}
