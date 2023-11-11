package ru.extreme.bot.clickbot.enums;

import lombok.Getter;

@Getter
public enum EndPoint {

    USER("user"),
    ACCOUNTS("accounts");

    private final String name;

    EndPoint(String name) {
        this.name = name;
    }
}
