package ru.extreme.bot.clickbot.utils;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс для создания отправляемых сообщений
 */
public final class MessageUtils {

    private MessageUtils() {
    }

    public static SendMessage createSendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        return sendMessage;
    }

    public static SendMessage createSendMessageMarkup(Long chatId, String text, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }

    public static EditMessageText createEditMessage(Long chatId, String text, Integer messageId) {
        EditMessageText editMessage = new EditMessageText();

        editMessage.setChatId(chatId);
        editMessage.setText(text);
        editMessage.setMessageId(messageId);

        return editMessage;
    }

    public static EditMessageText createEditMessageMarkup(Long chatId, String text,
                                                    Integer messageId, InlineKeyboardMarkup markup) {
        EditMessageText editMessage = new EditMessageText();

        editMessage.setChatId(chatId);
        editMessage.setText(text);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(markup);

        return editMessage;
    }

    public static EditMessageText createMessageMarkupClickProfilesInfo(Long chatId, String text, Integer messageId,
                                                                       Map<ClickProfile, List<ProfileAccount>> profileAccountsMap,
                                                                       InlineKeyboardMarkup markup){

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(markup);
        editMessage.setText(profilesInfo(text, profileAccountsMap));

        return editMessage;
    }

    public static List<SendMessage> createMessageClickProfilesInfo(List<Long> chatsId, String text, InlineKeyboardMarkup markup,
                                                             Map<ClickProfile, List<ProfileAccount>> profileAccountsMap){

        List<SendMessage> messages = new ArrayList<>();

        for (Long chatId: chatsId) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(markup);
            sendMessage.setText(profilesInfo(text, profileAccountsMap));

            messages.add(sendMessage);

        }

        return messages;
    }

    private static String profilesInfo (String text, Map<ClickProfile, List<ProfileAccount>> profileAccountsMap) {
        StringBuilder infoBuilder = new StringBuilder(text);

        for (Map.Entry<ClickProfile, List<ProfileAccount>> entry : profileAccountsMap.entrySet()) {
            ClickProfile profile = entry.getKey();
            List<ProfileAccount> accounts = entry.getValue();

            infoBuilder.append("\n");
            infoBuilder.append(EmojiParser.parseToUnicode(":briefcase:")).append(profile.getDescription())
                    .append("\n").append("Баланс профиля - ")
                    .append(String.format("%.2f", profile.getBalance()))
                    .append(" р.\n").append("\n")
                    .append("Балансы аккаунтов:\n")
                    .append(accountBalances(accounts)).append("------------------------------");
        }

        return infoBuilder.toString();
    }


    private static String accountBalances(List<ProfileAccount> accounts) {
        StringBuilder result = new StringBuilder();

        for (ProfileAccount account : accounts) {
            result.append(account.getService()).append(" - ").append(String.format("%.2f",account.getBalance())).append(" р.\n");
        }

        return result.toString();
    }

}
