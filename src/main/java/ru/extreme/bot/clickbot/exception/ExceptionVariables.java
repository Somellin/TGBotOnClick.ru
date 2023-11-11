package ru.extreme.bot.clickbot.exception;

import lombok.Getter;

@Getter
public enum ExceptionVariables {
    ADD_ADMIN_EXCEPTION("Проверьте введенные данные и попробуйте еще раз."),
    DELETE_ADMIN_EXCEPTION("Выберите удаляемого администратора из списка."),
    ADD_ACCOUNT_EXCEPTION("Проверьте правильность написания токена и попробуйте еще раз."),
    DELETE_ACCOUNT_EXCEPTION("Выберите удаляемый аккаунт из списка."),
    SHOW_PROFILE_EXCEPTION("Выберите профиль из списка"),
    UPDATE_DATA_EXCEPTION("При обновлении данных что то пошло не так, попробуйте еще раз."),
    CLIENT_EXCEPTION("Ошибка получения данных с Click.ru.");

    private final String code;

    ExceptionVariables(String code) {
        this.code = code;
    }
}
