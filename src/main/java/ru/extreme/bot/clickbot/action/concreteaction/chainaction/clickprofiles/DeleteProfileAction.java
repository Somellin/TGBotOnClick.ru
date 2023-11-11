package ru.extreme.bot.clickbot.action.concreteaction.chainaction.clickprofiles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.ChainAction;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.exception.ExceptionVariables;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collections;
import java.util.List;

import static ru.extreme.bot.clickbot.action.actionenum.ChainActionCode.DELETE_PROFILE_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.CANCEL_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.exception.ExceptionVariables.DELETE_ACCOUNT_EXCEPTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;

/**
 * Обработчик команды добавления профиля
 */
@Component
@RequiredArgsConstructor
public class DeleteProfileAction implements ChainAction {

    private static final List<ActionCode> ACTION_LIST = Collections.singletonList(MENU_ACTION);
    private static final List<ActionCode> CANCEL_LIST = Collections.singletonList(CANCEL_ACTION);

    private final ClickProfileService clickProfileService;

    @Override
    public String getCode() {
        return DELETE_PROFILE_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        List<ClickProfile> profiles = clickProfileService.findAllProfiles();

        return createEditMessageMarkup(info.getChatId(),
                "Выберите профиль для удаления",
                info.getMessageId(),
                MarkupCreator.createMarkupProfilesList(profiles, CANCEL_LIST));
    }

    @Override
    public BotApiMethod callback(Update update) throws ServiceException {
        try {
            MessageInfo info = new MessageInfo(update);

            if (!info.isCallBack()) {
                throw new Exception();
            }

            Long profileId = Long.valueOf(info.getData());
            String profileName = clickProfileService.deleteProfileById(profileId);

            return createEditMessageMarkup(info.getChatId(),
                    String.format("Профиль %s удален", profileName),
                    info.getMessageId(),
                    MarkupCreator.createMarkupActions(ACTION_LIST));
        } catch (Exception e) {
            throw new ServiceException(DELETE_ACCOUNT_EXCEPTION.getCode(), e);
        }

    }
}
