package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import java.util.List;
import java.util.Map;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;
import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.SHOW_ALL_DATA_ACTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createMessageMarkupClickProfilesInfo;

@Component
@RequiredArgsConstructor
public class ShowAllDataAction implements SingleAction {
    private static final List<ActionCode> ACTION_LIST = List.of(MENU_ACTION);

    private final ClickProfileService clickProfileService;

    @Override
    public String getCode() {
        return SHOW_ALL_DATA_ACTION.getCode();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        MessageInfo info = new MessageInfo(update);
        Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = clickProfileService.getProfileAccountsMap();

        List<BotApiMethod> sendMessage = createMessageMarkupClickProfilesInfo(info.getChatId(), "Данные профилей",
                profileAccountsMap, MarkupCreator.createMarkupActions(ACTION_LIST));

        return sendMessage;
    }

}
