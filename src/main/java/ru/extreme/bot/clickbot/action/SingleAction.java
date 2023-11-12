package ru.extreme.bot.clickbot.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Интерфейс для обработки одиночных команд
 */
public interface SingleAction {

    String getCode();

    List<BotApiMethod> handle(Update update);

}
