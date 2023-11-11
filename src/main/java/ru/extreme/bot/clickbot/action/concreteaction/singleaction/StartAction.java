package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.enums.Role;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;
import ru.extreme.bot.clickbot.utils.MessageInfo;
import ru.extreme.bot.clickbot.utils.extra.Variables;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.START_ACTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createSendMessage;

/**
 * Обработчик команды /start
 */
@Component
@RequiredArgsConstructor
public class StartAction implements SingleAction {
    private final ChatUserService chatUserService;

    @Override
    public String getCode() {
        return START_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);

        Role currentUserRole = chatUserService.chatUserIsRegisteredAndGetRoel(info.getChatId());
        String userFirstName = update.getMessage().getChat().getFirstName();

        SendMessage sendMessage;
        if (currentUserRole.equals(Role.NONE_REGISTERED)) {
            chatUserService.registerUser(update.getMessage().getChat());

            sendMessage = createSendMessage(info.getChatId(),
                    String.format(Variables.START_NEW_USER_TEXT.getMsg(), userFirstName));
        } else {
            sendMessage = createSendMessage(info.getChatId(),
                    String.format(Variables.START_TEXT.getMsg(), userFirstName));;
        }

        return sendMessage;
    }
}