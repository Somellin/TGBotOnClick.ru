package ru.extreme.bot.clickbot.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;
import ru.extreme.bot.clickbot.service.click.ProfileAccountService;
import ru.extreme.bot.clickbot.utils.MessageUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.MENU_ACTION;

@Component
@RequiredArgsConstructor
public class UpdateAllProfilesJob {
    private static final List<ActionCode> ACTION_LIST = Collections.singletonList(MENU_ACTION);
    private static final String ACTION_TEXT = "Ежедневное обновление базы. Список профилей с балансом меньше 5000 р.";

    private final ClickProfileService clickProfileService;
    private final ProfileAccountService profileAccountService;
    private final ChatUserService chatUserService;

    public List<SendMessage> updatedProfilesWithBalanceLess5000() throws ServiceException, InterruptedException {
        clickProfileService.updateAllProfilesData();

        List<Long> adminsChatId = chatUserService.getChatIdsAllAdmins();

        List<Long> profilesID = profileAccountService.getProfilesIDWhereAccBalanceLess5000();
        Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = clickProfileService.getProfileAccountsMapById(profilesID);


        return MessageUtils.createMessageClickProfilesInfo(adminsChatId, ACTION_TEXT, MarkupCreator.createMarkupActions(ACTION_LIST),
                profileAccountsMap);
    }

}
