package ru.extreme.bot.clickbot.configuration;

import okhttp3.OkHttpClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.extreme.bot.clickbot.controller.ClickBotController;

/**
 * Класс конфигурации
 */
@Configuration
@EnableCaching
@EnableScheduling
public class ClickBotConfiguration {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(ClickBotController clickBot){
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(clickBot);
            return api;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
