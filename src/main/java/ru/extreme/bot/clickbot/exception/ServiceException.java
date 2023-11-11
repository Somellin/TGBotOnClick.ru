package ru.extreme.bot.clickbot.exception;

/**
 * Кастомные ошибки
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
