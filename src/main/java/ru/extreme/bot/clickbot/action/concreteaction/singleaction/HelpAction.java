package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.HELP_ACTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createSendMessage;

/**
 * Обработчик команды /help
 */
@Component
public class HelpAction implements SingleAction {

    private static final String HELP_TEXT =
            "Данный бот служит для отслеживания остатков баланса на Click.ru. \n" +
            "Основные команды :\n" +
            "/start - команда для начала работы с ботом и регистрации.\n" +
            "/menu - основная команда, через меню выполняются все требуемые задачи.\n" +
            "/help - команда для помощи (это сообщение)\n" +
            "\n\n" +
            "Чтобы взаимодействовать с ботом требуются права администратора.\n\n";

    @Override
    public String getCode() {
        return HELP_ACTION.getCode();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        return Collections.singletonList(createSendMessage(info.getChatId(), HELP_TEXT));
    }
}
