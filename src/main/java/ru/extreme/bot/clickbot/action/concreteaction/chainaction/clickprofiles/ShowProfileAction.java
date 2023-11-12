package ru.extreme.bot.clickbot.action.concreteaction.chainaction.clickprofiles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.ChainAction;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.action.actionenum.ChainActionCode;
import ru.extreme.bot.clickbot.exception.ExceptionVariables;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;
import ru.extreme.bot.clickbot.service.click.ProfileAccountService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.CANCEL_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.exception.ExceptionVariables.SHOW_PROFILE_EXCEPTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessageMarkup;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createMessageMarkupClickProfilesInfo;

/**
 * Обработчик команды просмотра профиля
 */
@Component
@RequiredArgsConstructor
public class ShowProfileAction implements ChainAction {

    private static final List<ActionCode> ACTION_LIST = Collections.singletonList(MENU_ACTION);
    private static final List<ActionCode> CANCEL_LIST = Collections.singletonList(CANCEL_ACTION);

    private final ClickProfileService clickProfileService;
    private final ProfileAccountService profileAccountService;

    @Override
    public String getCode() {
        return ChainActionCode.SHOW_PROFILE_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        List<ClickProfile> profiles = clickProfileService.findAllProfiles();

        return createEditMessageMarkup(info.getChatId(),
                "Выберите профиль для просмотра",
                info.getMessageId(),
                MarkupCreator.createMarkupProfilesList(profiles, CANCEL_LIST));
    }

    @Override
    public List<BotApiMethod> callback(Update update) throws ServiceException {
        try {
            MessageInfo info = new MessageInfo(update);

            if (!info.isCallBack()) {
                throw new Exception();
            }

            Long profileId = Long.valueOf(info.getData());

            ClickProfile profile = clickProfileService.getClickProfileById(profileId);
            List<ProfileAccount> accounts = profileAccountService.findProfileAccountsByProfileId(profileId);

            Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = new HashMap<>();
            profileAccountsMap.put(profile, accounts);

            return createMessageMarkupClickProfilesInfo(info.getChatId(),
                    "Информация по профилю:",
                    profileAccountsMap, MarkupCreator.createMarkupActions(ACTION_LIST));
        } catch (Exception e) {
            throw new ServiceException(SHOW_PROFILE_EXCEPTION.getCode(), e);
        }

    }
}
