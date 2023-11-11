package ru.extreme.bot.clickbot.action.actionenum;

/**
 * Список цепных команд
 */
public enum ChainActionCode implements ActionCode {

    ADD_PROFILE_ACTION("ADD_PROFILE_ACTION", "Добавить/обновить профиль"),
    DELETE_PROFILE_ACTION("DELETE_PROFILE_ACTION", "Удалить профиль"),
    ADD_ADMIN_ACTION("ADD_ADMIN_ACTION", "Добавить администратора"),
    DELETE_ADMIN_ACTION("DELETE_ADMIN_ACTION", "Удалить администратора"),
    SHOW_PROFILE_ACTION("SHOW_PROFILE_ACTION", "Посмотреть данные по профилю"),
    UNKNOWN("UNKNOWN", "Неизвестная команда");

    private final String code;
    private final String name;

    ChainActionCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static ChainActionCode getByCode(String code){
        for(ChainActionCode action : values()) {
            if(action.getCode().equals(code))
                return action;
        }

        return UNKNOWN;
    }
}
