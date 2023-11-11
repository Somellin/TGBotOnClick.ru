package ru.extreme.bot.clickbot.markup.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Интерфейс для создания кнопок в сообщении
 */
public interface MarkupKeyboardBuilder {

    /**
     * Создание объекта отображения кнопок
     */
    MarkupKeyboardBuilder createMarkup();

    /**
     * Добавление новой матрицы кнопок
     */
    MarkupKeyboardBuilder addKeyBoard();

    /**
     * Добавление нового ряда кнопок
     */
    MarkupKeyboardBuilder addRowButton(List<InlineKeyboardButton> row);

    /**
     * Результат построения меню
     */
    InlineKeyboardMarkup build();

}
