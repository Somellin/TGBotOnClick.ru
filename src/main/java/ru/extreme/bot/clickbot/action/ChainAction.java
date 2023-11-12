package ru.extreme.bot.clickbot.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.exception.ServiceException;

import java.util.List;

/**
 * Интерфейс для обработки цепных команд
 */
public interface ChainAction {
    String getCode();

    BotApiMethod handle(Update update);

    List<BotApiMethod> callback(Update update) throws Exception;
}
