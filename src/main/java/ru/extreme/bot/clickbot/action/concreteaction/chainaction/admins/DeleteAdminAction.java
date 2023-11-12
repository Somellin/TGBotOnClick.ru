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
import ru.extreme.bot.clickbot.model.ChatUser;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collections;
import java.util.List;

import static ru.extreme.bot.clickbot.action.actionenum.ChainActionCode.DELETE_ADMIN_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.CANCEL_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.exception.ExceptionVariables.DELETE_ADMIN_EXCEPTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;

/**
 * Обработчик команды удаление администратора
 */
@Component
@RequiredArgsConstructor
public class DeleteAdminAction implements ChainAction {

    private static final List<ActionCode> ACTION_LIST = Collections.singletonList(MENU_ACTION);
    private static final List<ActionCode> CANCEL_LIST = Collections.singletonList(CANCEL_ACTION);

    private final ChatUserService chatUserService;

    @Override
    public String getCode() {
        return DELETE_ADMIN_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        List<ChatUser> admins = chatUserService.findAllAdminsName(info.getChatId());

        return createEditMessageMarkup(info.getChatId(),
                "Выберите действующего администратора для удаления",
                info.getMessageId(),
                MarkupCreator.createMarkupAdminsList(admins, CANCEL_LIST));
    }

    @Override
    public List<BotApiMethod> callback(Update update) throws Exception {
        try {
            MessageInfo info = new MessageInfo(update);

            if (!info.isCallBack()) {
                throw new Exception();
            }

            Long adminId = Long.valueOf(info.getData());

            String adminName = chatUserService.deleteAdminById(adminId);

            return Collections.singletonList(createEditMessageMarkup(info.getChatId(),
                    adminName + " удален.",
                    info.getMessageId(),
                    MarkupCreator.createMarkupActions(ACTION_LIST)));

        } catch (Exception e) {
            throw new ServiceException(DELETE_ADMIN_EXCEPTION.getCode(), e);
        }
    }
}
