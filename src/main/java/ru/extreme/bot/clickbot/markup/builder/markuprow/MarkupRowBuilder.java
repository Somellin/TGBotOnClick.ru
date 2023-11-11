package ru.extreme.bot.clickbot.markup.builder.markuprow;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Интерфейс для создания строк кнопок
 */
public interface MarkupRowBuilder {

    /**
     * Создание строки для кнопок в сообщении
     */
    MarkupRowBuilder createNewRow();

    /**
     * Добавление кнопки в строку
     */
    MarkupRowBuilder addButton(String textButton, String callbackButton);

    /**
     * Готовая строка кнопок
     */
    List<InlineKeyboardButton> build();
}
