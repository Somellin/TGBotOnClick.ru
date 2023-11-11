package ru.extreme.bot.clickbot.action.actionenum;

/**
 * Список одиночных команд
 */
public enum SingleActionCode implements ActionCode {

    START_ACTION("START_ACTION", "Начало работы"),
    HELP_ACTION("HELP_ACTION", "Помощь"),
    MENU_ACTION("MENU_ACTION", "Меню"),
    SETTINGS_ACTION("SETTINGS_ACTION", "Настройки"),
    SHOW_MY_DATA_ACTION("SHOW_MY_DATA_ACTION","Показать мои данные"),
    CANCEL_ACTION("CANCEL_ACTION", "Отмена"),
    UPDATE_DATA_ACTION("UPDATE_DATA_ACTION", "Обновить данные базы"),
    UNKNOWN("UNKNOWN", "Неизвестная команда");

    private final String code;
    private final String name;

    SingleActionCode(String code, String name) {
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


    public static SingleActionCode getByCode(String code){
        for(SingleActionCode action : values()) {
            if(action.getCode().equals(code))
                return action;
        }

        return UNKNOWN;
    }
}
