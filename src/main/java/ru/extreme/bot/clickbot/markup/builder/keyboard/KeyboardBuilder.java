package ru.extreme.bot.clickbot.markup.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Билдер для создания кнопок в сообщении
 */
public class KeyboardBuilder implements MarkupKeyboardBuilder {

    private InlineKeyboardMarkup markup;
    private List<List<InlineKeyboardButton>> keyboard;

    public KeyboardBuilder() {
        super();
    }

    @Override
    public MarkupKeyboardBuilder createMarkup() {
        this.markup = new InlineKeyboardMarkup();
        return this;
    }

    @Override
    public MarkupKeyboardBuilder addKeyBoard() {
        this.keyboard = new ArrayList<>();
        return this;
    }

    @Override
    public MarkupKeyboardBuilder addRowButton(List<InlineKeyboardButton> row) {
        this.keyboard.add(row);
        return this;
    }

    @Override
    public InlineKeyboardMarkup build() {
        this.markup.setKeyboard(this.keyboard);
        return this.markup;
    }
}
