package ru.extreme.bot.clickbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.extreme.bot.clickbot.utils.TokenEncoder;

@SpringBootApplication
public class CkickBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(CkickBotApplication.class, args);
    }
}