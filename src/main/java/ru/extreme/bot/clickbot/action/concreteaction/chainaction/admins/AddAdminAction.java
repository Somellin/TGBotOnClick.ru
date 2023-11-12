package ru.extreme.bot.clickbot.action.concreteaction.chainaction.admins;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.ChainAction;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.exception.ExceptionVariables;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collections;
import java.util.List;

import static ru.extreme.bot.clickbot.action.actionenum.ChainActionCode.ADD_ADMIN_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.CANCEL_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.exception.ExceptionVariables.ADD_ADMIN_EXCEPTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createSendMessageMarkup;

/**
 * Обработчик команды добавления администратора
 */
@Component
@RequiredArgsConstructor
public class AddAdminAction implements ChainAction {

    private static final List<ActionCode> ACTION_LIST = Collections.singletonList(MENU_ACTION);
    private static final List<ActionCode> CANCEL_LIST = Collections.singletonList(CANCEL_ACTION);

    private final ChatUserService chatUserService;

    @Override
    public String getCode() {
        return ADD_ADMIN_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);

        return createEditMessageMarkup(info.getChatId(),
                "Введите ID пользователя для получения прав администратора.",
                info.getMessageId(), MarkupCreator.createMarkupActions(CANCEL_LIST));
    }

    @Override
    public List<BotApiMethod> callback(Update update) throws ServiceException {
        try {
            MessageInfo info = new MessageInfo(update);

            if (info.isCallBack()) {
                throw new Exception();
            }

            Long userId = Long.valueOf(info.getData());

            String userAccept = chatUserService.setUserAdminRightsById(userId);

            return Collections.singletonList(createSendMessageMarkup(info.getChatId(),
                    "Пользователь " + userAccept + " получил права администратора.",
                    MarkupCreator.createMarkupActions(ACTION_LIST)));
        } catch (Exception e) {
            throw new ServiceException(ADD_ADMIN_EXCEPTION.getCode(), e);
        }
    }
}
