package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

import static ru.extreme.bot.clickbot.action.actionenum.ChainActionCode.SHOW_PROFILE_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.*;
import static ru.extreme.bot.clickbot.enums.Role.ADMIN;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createSendMessageMarkup;

/**
 * Обработчик команды /menu
 */
@Component
@RequiredArgsConstructor
public class MenuAction implements SingleAction {
    private static final List<ActionCode> ADMIN_ACTION_LIST = List
            .of(UPDATE_DATA_ACTION, SHOW_PROFILE_ACTION, SHOW_ALL_DATA_ACTION, SETTINGS_ACTION);

    private static final List<ActionCode> NONE_ADMIN_ACTION_LIST = Collections.singletonList(SETTINGS_ACTION);

    private final ChatUserService chatUserService;

    @Override
    public String getCode() {
        return MENU_ACTION.getCode();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        Role currentUserRole = chatUserService.chatUserIsRegisteredAndGetRoel(info.getChatId());

        return Collections.singletonList(messageMenu(currentUserRole, info));
    }

    private BotApiMethod messageMenu(Role currentUserRole, MessageInfo info) {
        if (info.isCallBack()) {
            EditMessageText messageEdit;
            if (currentUserRole.equals(ADMIN)) {
                messageEdit = createEditMessageMarkup(info.getChatId(),
                        "Меню",
                        info.getMessageId(), MarkupCreator.createMarkupActions(ADMIN_ACTION_LIST));
            } else {
                messageEdit = createEditMessageMarkup(info.getChatId(),
                        "Не достаточно прав, получите свой ID в настройках и отправьте администратору чтобы получить права.",
                        info.getMessageId(), MarkupCreator.createMarkupActions(NONE_ADMIN_ACTION_LIST));
            }

            return messageEdit;
        } else {
            SendMessage sendMessage;
            if (currentUserRole.equals(ADMIN)) {
                sendMessage = createSendMessageMarkup(info.getChatId(),
                        "Меню",
                        MarkupCreator.createMarkupActions(ADMIN_ACTION_LIST));
            } else {
                sendMessage = createSendMessageMarkup(info.getChatId(),
                        "Не достаточно прав, получите свой ID в настройках и отправьте администратору чтобы получить права.",
                        MarkupCreator.createMarkupActions(NONE_ADMIN_ACTION_LIST));
            }

            return sendMessage;
        }
    }
}
