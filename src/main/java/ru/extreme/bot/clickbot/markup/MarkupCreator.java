package ru.extreme.bot.clickbot.markup;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.extreme.bot.clickbot.action.actionenum.ActionCode;
import ru.extreme.bot.clickbot.markup.builder.markuprow.MarkupRowBuilder;
import ru.extreme.bot.clickbot.markup.builder.markuprow.RowBuilder;
import ru.extreme.bot.clickbot.markup.builder.keyboard.KeyboardBuilder;
import ru.extreme.bot.clickbot.markup.builder.keyboard.MarkupKeyboardBuilder;
import ru.extreme.bot.clickbot.model.ChatUser;
import ru.extreme.bot.clickbot.model.ClickProfile;

import java.util.List;

/**
 * Класс для создания интерфейса в сообщении
 */
public final class MarkupCreator {
    private static final MarkupKeyboardBuilder markupBuilder = new KeyboardBuilder();

    private MarkupCreator() {
    }

    public static InlineKeyboardMarkup createMarkupActions(List<ActionCode> actions) {
        InlineKeyboardMarkup markup = markupBuilder.createMarkup().addKeyBoard().build();

        fillMarkupActions(markup, actions);

        return markup;
    }

    public static InlineKeyboardMarkup createMarkupAdminsList(List<ChatUser> admins, List<ActionCode> actions) {
        InlineKeyboardMarkup markup = markupBuilder.createMarkup().addKeyBoard().build();

        fillMarkupAdminList(markup, admins);
        fillMarkupActions(markup, actions);

        return markup;
    }

    public static InlineKeyboardMarkup createMarkupProfilesList(List<ClickProfile> profiles, List<ActionCode> actions) {
        InlineKeyboardMarkup markup = markupBuilder.createMarkup().addKeyBoard().build();

        fillMarkupProfileList(markup, profiles);
        fillMarkupActions(markup, actions);

        return markup;
    }

    private static void addRowButton(InlineKeyboardMarkup markup, String textButton, String callbackData) {
        MarkupRowBuilder markupRowBuilder = new RowBuilder();
        markup.getKeyboard().add(
                markupRowBuilder.createNewRow().addButton(textButton, callbackData).build()
        );
    }

    private static void fillMarkupActions(InlineKeyboardMarkup markup, List<ActionCode> actions) {
        for (ActionCode action : actions) {
            addRowButton(markup, action.getName(), action.getCode());
        }
    }

    private static void fillMarkupAdminList(InlineKeyboardMarkup markup, List<ChatUser> admins) {
        for (ChatUser admin : admins) {
            addRowButton(markup, admin.getFirstName(), admin.getChatId().toString());
        }
    }

    private static void fillMarkupProfileList(InlineKeyboardMarkup markup, List<ClickProfile> profiles) {
        for (ClickProfile profile : profiles) {
            addRowButton(markup, profile.getDescription(), profile.getId().toString());
        }
    }
}
