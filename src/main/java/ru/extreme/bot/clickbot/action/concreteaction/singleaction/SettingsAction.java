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

import static ru.extreme.bot.clickbot.action.actionenum.ChainActionCode.*;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.*;
import static ru.extreme.bot.clickbot.enums.Role.ADMIN;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;

/**
 * Обработчик команды настройки
 */
@Component
@RequiredArgsConstructor
public class SettingsAction implements SingleAction {
    private static final List<ActionCode> ADMIN_ACTION_LIST = List
            .of(ADD_PROFILE_ACTION, DELETE_PROFILE_ACTION,
                    ADD_ADMIN_ACTION, DELETE_ADMIN_ACTION,
                    SHOW_MY_DATA_ACTION, MENU_ACTION);
    private static final List<ActionCode> NONE_ADMIN_ACTION_LIST = List
            .of(SHOW_MY_DATA_ACTION, MENU_ACTION);

    private final ChatUserService chatUserService;

    @Override
    public String getCode() {
        return SETTINGS_ACTION.getCode();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        Role currentUserRole = chatUserService.chatUserIsRegisteredAndGetRoel(info.getChatId());

        EditMessageText messageEdit;
        if (currentUserRole.equals(ADMIN)) {
            messageEdit = createEditMessageMarkup(info.getChatId(),
                    "Настройки",
                    info.getMessageId(),
                    MarkupCreator.createMarkupActions(ADMIN_ACTION_LIST));
        } else {

            messageEdit = createEditMessageMarkup(info.getChatId(),
                    "Настройки",
                    info.getMessageId(),
                    MarkupCreator.createMarkupActions(NONE_ADMIN_ACTION_LIST));
        }

        return Collections.singletonList(messageEdit);
    }
}
