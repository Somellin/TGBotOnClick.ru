package ru.extreme.bot.clickbot.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для обработки одиночных команд
 */
public interface SingleAction {

    String getCode();

    BotApiMethod handle(Update update);
}
