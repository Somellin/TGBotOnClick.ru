package ru.extreme.bot.clickbot.action.concreteaction.singleaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.utils.MessageInfo;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.UPDATE_DATA_ACTION;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createEditMessage;

@Component
@RequiredArgsConstructor
public class UpdateDataAction implements SingleAction {

    @Override
    public String getCode() {
        return UPDATE_DATA_ACTION.getCode();
    }

    @Override
    public BotApiMethod handle(Update update) {
        MessageInfo info = new MessageInfo(update);

        return createEditMessage(info.getChatId(),
                "Обновление данных запущено. Нужно подождать",
                info.getMessageId());
    }
}
