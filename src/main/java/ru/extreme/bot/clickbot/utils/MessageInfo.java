package ru.extreme.bot.clickbot.utils;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class MessageInfo {

    private CallbackQuery callbackQuery;
    private Message message;
    private Long chatId;
    private Integer messageId;
    private String data;
    private Chat chat;
    private boolean isCallBack;

    public MessageInfo(Update update) {
        if (update.hasMessage()) {
            this.message = update.getMessage();
            this.chatId = message.getChatId();
            this.data = message.getText();
            this.isCallBack = false;
        } else if (update.hasCallbackQuery()) {
            this.callbackQuery = update.getCallbackQuery();
            this.message = callbackQuery.getMessage();
            this.chatId = message.getChatId();
            this.messageId = message.getMessageId();
            this.data = callbackQuery.getData();
            this.chat = message.getChat();
            this.isCallBack = true;
        }

    }
}
