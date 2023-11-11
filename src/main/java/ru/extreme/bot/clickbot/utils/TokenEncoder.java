package ru.extreme.bot.clickbot.utils;

import java.util.Base64;

/**
 * Класс для работы с токенами
 */
public final class TokenEncoder {

    private TokenEncoder() {
    }

    /**
     * Шифрование токена
     */
    public static String encodeToken(String token){
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    /**
     * Расшифровка токена
     */
    public static String decodeToken(String encodeToken){
        byte[] decodedTokenBytes = Base64.getDecoder().decode(encodeToken);
        return new String(decodedTokenBytes);
    }
}
