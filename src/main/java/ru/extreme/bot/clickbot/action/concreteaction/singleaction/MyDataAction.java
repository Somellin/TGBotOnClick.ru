package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.enums.Role;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collections;
import java.util.List;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.SHOW_MY_DATA_ACTION;
import static ru.extreme.bot.clickbot.enums.Role.NONE_REGISTERED;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessage;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;

/**
 * Обработчик команды получения данных пользователя чата
 */
@Component
@RequiredArgsConstructor
public class MyDataAction implements SingleAction {
    private static final List<ActionCode> ACTION_LIST = List.of(MENU_ACTION);

    @Override
    public String getCode() {
        return SHOW_MY_DATA_ACTION.getCode();
    }

    private final ChatUserService chatUserService;

    @Override
    public List<BotApiMethod> handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        Role currentUserRole = chatUserService.chatUserIsRegisteredAndGetRoel(info.getChatId());

        EditMessageText messageEdit;
        if (currentUserRole.equals(NONE_REGISTERED)) {
            messageEdit = createEditMessage(info.getChatId(),
                    "Вы не зарегистрированы, начните с команды /start",
                    info.getMessageId());
        } else {
            String myData = "Мои данные: \n" +
                    "ID: " + info.getChatId() + "\n" +
                    "Имя: " + info.getChat().getFirstName() + "\n" +
                    "Фамилия: " + info.getChat().getLastName() + "\n";

            messageEdit = createEditMessageMarkup(info.getChatId(), myData, info.getMessageId(),
                    MarkupCreator.createMarkupActions(ACTION_LIST));
        }

        return Collections.singletonList(messageEdit);
    }
}
