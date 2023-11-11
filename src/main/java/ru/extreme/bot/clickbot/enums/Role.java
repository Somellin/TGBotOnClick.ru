package ru.extreme.bot.clickbot.enums;

import lombok.Getter;

@Getter
public enum Role {

    NONE_REGISTERED("NONE_REGISTERED"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
