package ru.extreme.bot.clickbot.utils.extra;

import lombok.Getter;

/**
 * Список вспомогательных переменных
 */
@Getter
public enum Variables {

    START_TEXT("Привет! %s \nВы уже зарегистрированы, подробности можете узнать по команде /help"),
    START_NEW_USER_TEXT("Привет! %s \nВы успешно зарегистрировались, подробности можете узнать по команде /help"),
    HELP_TEXT("Данный бот служит для отслеживания остатков баланса на Click.ru. \n" +
            "Основные команды :\n" +
            "/start - команда для начала работы с ботом и регистрации.\n" +
            "/menu - основная команда, через меню выполняются все требуемые задачи.\n" +
            "/help - команда для помощи (это сообщение)\n" +
            "\n\n" +
            "Чтобы взаимодействовать с ботом требуются права администратора.\n\n"),
    ;

    private final String msg;

    Variables(String msg) {
        this.msg = msg;
    }
}
