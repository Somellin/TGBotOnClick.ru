package ru.extreme.bot.clickbot.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Services {

    DIRECT("DIRECT"),
    ADWORDS("ADWORDS"),
    MYTARGET("MYTARGET"),
    VK("VK"),
    FB("FB"),
    YMAPS("YMAPS"),
    TIKTOK("TIKTOK"),
    YNAVIGATOR("YNAVIGATOR"),
    YZEN("YZEN"),
    AVITO("AVITO"),
    TELEGRAM("TELEGRAM"),
    YREALTY("YREALTY"),
    VK_ADS("VK_ADS"),
    DOUBLEGIS("DOUBLEGIS"),
    AVITO_MEDIA("AVITO_MEDIA"),
    SEARCH_ENGINES_LEADS("SEARCH_ENGINES_LEADS"),
    VK_MARKET_PLATFORM("VK_MARKET_PLATFORM")
    ;

    private final String name;

    Services(String name) {
        this.name = name;
    }

    public static List<String> mainServices () {
       return Arrays.asList(DIRECT.getName(), VK_ADS.getName());
    }
}
