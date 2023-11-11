package ru.extreme.bot.clickbot.markup.builder.markuprow;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Билдер для создания строк кнопок
 */
public class RowBuilder implements MarkupRowBuilder {

    private List<InlineKeyboardButton> row;

    public RowBuilder() {
        super();
    }

    @Override
    public MarkupRowBuilder createNewRow() {
        this.row = new ArrayList<>();
        return this;
    }

    @Override
    public MarkupRowBuilder addButton(String textButton, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(textButton);
        button.setCallbackData(callbackData);

        this.row.add(button);

        return this;
    }

    @Override
    public List<InlineKeyboardButton> build() {
        return this.row;
    }
}
