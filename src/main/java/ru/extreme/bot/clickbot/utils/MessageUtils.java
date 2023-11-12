package ru.extreme.bot.clickbot.utils;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
    private static final int MESS_MAX_LENGTH = 4096;
    private static final int PROFILE_INFO_LENGTH = 160;

    private MessageUtils() {
    }

    public static SendMessage createEmptyMessage(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("");

        return sendMessage;
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

    public static List<BotApiMethod> createMessageMarkupClickProfilesInfo(Long chatId, String text,
                                                                          Map<ClickProfile, List<ProfileAccount>> profileAccountsMap,
                                                                          InlineKeyboardMarkup markup) {

        List<BotApiMethod> infoMessages = new ArrayList<>();

        for (Map.Entry<ClickProfile, List<ProfileAccount>> entry : profileAccountsMap.entrySet()) {
            ClickProfile profile = entry.getKey();
            List<ProfileAccount> accounts = entry.getValue();

            addOrUpdateInfoMessageForLength(infoMessages, profile, accounts, chatId, text);
        }

        ((SendMessage) infoMessages.get(infoMessages.size() - 1)).setReplyMarkup(markup);

        return infoMessages;
    }

    public static List<BotApiMethod> createMessageClickProfilesInfo(List<Long> chatsId, String text, InlineKeyboardMarkup markup,
                                                                   Map<ClickProfile, List<ProfileAccount>> profileAccountsMap) {

        List<BotApiMethod> infoMessages = new ArrayList<>();

        for (Long chatId : chatsId) {
            infoMessages.addAll(createMessageMarkupClickProfilesInfo(chatId,  text,
                     profileAccountsMap, markup));
        }

        return infoMessages;
    }

    private static void addOrUpdateInfoMessageForLength(List<BotApiMethod> infoMessages, ClickProfile profile,
                                                        List<ProfileAccount> accounts, Long chatId, String text) {
        int currentMessageIndex = infoMessages.size();

        SendMessage sendMessage;
        if (currentMessageIndex == 0) {
            sendMessage = createSendMessage(chatId, text);
            infoMessages.add(sendMessage);
        } else {
            sendMessage = (SendMessage) infoMessages.get(currentMessageIndex - 1);
        }

        if (sendMessage.getText().length() + PROFILE_INFO_LENGTH < MESS_MAX_LENGTH) {
            if (currentMessageIndex == 0) {
                infoMessages.remove(currentMessageIndex);
            } else {
                infoMessages.remove(currentMessageIndex - 1);
            }

            sendMessage.setText(profilesInfo(sendMessage.getText(), profile, accounts));
            infoMessages.add(sendMessage);
        } else {
            infoMessages.add(createEmptyMessage(chatId));
        }
    }

    private static String profilesInfo(String text, ClickProfile profile, List<ProfileAccount> accounts) {
        StringBuilder infoBuilder = new StringBuilder(text);

        infoBuilder.append("\n");
        infoBuilder.append(EmojiParser.parseToUnicode(":briefcase:")).append(profile.getDescription())
                .append("\n").append("Баланс профиля - ")
                .append(String.format("%.2f", profile.getBalance()))
                .append(" р.\n").append("\n")
                .append("Балансы аккаунтов:\n")
                .append(accountBalances(accounts)).append("------------------------------");

        return infoBuilder.toString();
    }


    private static String accountBalances(List<ProfileAccount> accounts) {
        StringBuilder result = new StringBuilder();

        for (ProfileAccount account : accounts) {
            result.append(account.getService()).append(" - ").append(String.format("%.2f", account.getBalance())).append(" р.\n");
        }

        return result.toString();
    }

}
