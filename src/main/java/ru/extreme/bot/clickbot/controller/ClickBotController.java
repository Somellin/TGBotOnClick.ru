package ru.extreme.bot.clickbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.extreme.bot.clickbot.action.ChainAction;
import ru.extreme.bot.clickbot.action.SingleAction;
import ru.extreme.bot.clickbot.action.actionenum.ChainActionCode;
import ru.extreme.bot.clickbot.action.actionenum.SingleActionCode;
import ru.extreme.bot.clickbot.action.concreteaction.singleaction.supportthread.UpdateDataActionSupport;
import ru.extreme.bot.clickbot.action.strategy.ChainActionStrategy;
import ru.extreme.bot.clickbot.action.strategy.SingleActionStrategy;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.job.UpdateAllProfilesJob;
import ru.extreme.bot.clickbot.markup.MarkupCreator;
import ru.extreme.bot.clickbot.model.ClickProfile;
import ru.extreme.bot.clickbot.model.ProfileAccount;
import ru.extreme.bot.clickbot.service.click.ClickProfileService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ru.extreme.bot.clickbot.action.actionenum.SingleActionCode.*;
import static ru.extreme.bot.clickbot.utils.MessageUtils.createMessageMarkupClickProfilesInfo;

/**
 * Класс обработчика команд бота
 */
@Slf4j
@Component
public class ClickBotController extends TelegramLongPollingBot {
    private static final String TIME_ZONE = "Europe/Moscow";
    private static final String TIME_JOB = "0 30 16 * * *";
    private final Map<String, String> chainBindingActions = new ConcurrentHashMap<>();
    private final SingleActionStrategy singleActionStrategy;
    private final ChainActionStrategy chainActionStrategy;
    private final UpdateDataActionSupport updateDataActionSupport;

    private final UpdateAllProfilesJob updateAllProfilesJob;

    private final ClickProfileService clickProfileService;

    @Autowired
    public ClickBotController(@Value("${bot.token}") String botToken,
                              SingleActionStrategy singleActionStrategy, ChainActionStrategy chainActionStrategy,
                              UpdateDataActionSupport updateDataActionSupport, UpdateAllProfilesJob updateAllProfilesJob,
                              ClickProfileService clickProfileService) {
        super(botToken);
        this.singleActionStrategy = singleActionStrategy;
        this.chainActionStrategy = chainActionStrategy;
        this.updateDataActionSupport = updateDataActionSupport;
        this.updateAllProfilesJob = updateAllProfilesJob;
        this.clickProfileService = clickProfileService;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", START_ACTION.getName()));
        listOfCommands.add(new BotCommand("/menu", MENU_ACTION.getName()));
        listOfCommands.add(new BotCommand("/help", HELP_ACTION.getName()));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "balance_monitoring_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = message.getChatId().toString();

            boolean containsChainKey = chainBindingActions.containsKey(chatId);
            if (containsChainKey) {
                try {
                    BotApiMethod msg = chainActionStrategy.getStrategy(ChainActionCode.getByCode(chainBindingActions.get(chatId)))
                            .callback(update);

                    chainBindingActions.remove(chatId);

                    sendMessage(msg);
                } catch (Exception e) {
                    sendMessage(getExceptionMsg(chatId, e.getMessage()));
                }
            } else {
                switch (messageText) {
                    case "/start" -> {
                        SingleAction action = singleActionStrategy.getStrategy(START_ACTION);
                        sendMessage(action.handle(update));
                    }
                    case "/menu" -> {
                        SingleAction action = singleActionStrategy.getStrategy(MENU_ACTION);
                        sendMessage(action.handle(update));
                    }
                    case "/help" -> {
                        SingleAction action = singleActionStrategy.getStrategy(HELP_ACTION);
                        sendMessage(action.handle(update));
                    }

                    default -> defaultCommand(message);
                }
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            Message callMsg = callbackQuery.getMessage();
            String callData = callbackQuery.getData();
            String chatId = callMsg.getChatId().toString();

            SingleActionCode singleActionKey = SingleActionCode.getByCode(callData);
            ChainActionCode chainActionKey = ChainActionCode.getByCode(callData);

            if (!singleActionKey.equals(SingleActionCode.UNKNOWN)) {
                try {
                    singleActionHandler(chatId, singleActionKey, update);
                } catch (Exception e) {
                    sendMessage(getExceptionMsg(chatId, e.getMessage()));
                }
            } else if (!chainActionKey.equals(ChainActionCode.UNKNOWN)) {
                try {
                    chainActionHandler(chatId, chainActionKey, update);
                } catch (Exception e) {
                    sendMessage(getExceptionMsg(chatId, e.getMessage()));
                }
            } else if (chainBindingActions.containsKey(chatId)) {
                try {
                    chainBindActionHandler(chatId, update);
                } catch (Exception e) {
                    sendMessage(getExceptionMsg(chatId, e.getMessage()));
                }
            }
        } else defaultCommand(message);
    }

    /**
     * Обработчик одиночных команд
     */
    private void singleActionHandler(String chatId, SingleActionCode singleActionKey, Update update) throws ServiceException {
        if (singleActionKey.equals(CANCEL_ACTION)) {
            singleActionKey = MENU_ACTION;
            chainBindingActions.remove(chatId);
        }

        SingleAction singleAction = singleActionStrategy.getStrategy(singleActionKey);
        sendMessage(singleAction.handle(update));

        if (singleActionKey.equals(UPDATE_DATA_ACTION)) {
            updateDataAndNotify(chatId, update);
        }
    }

    /**
     * Обработчик первой цепной команды
     */
    private void chainActionHandler(String chatId, ChainActionCode chainActionKey, Update update) throws Exception {
        ChainAction chainAction = chainActionStrategy.getStrategy(chainActionKey);

        if (chainAction != null) {
            var msg = chainAction.handle(update);
            chainBindingActions.put(chatId, chainAction.getCode());

            sendMessage(msg);
        } else if (chainBindingActions.containsKey(chatId)) {
            var msg = chainActionStrategy.getStrategy(ChainActionCode.getByCode(chainBindingActions.get(chatId)))
                    .callback(update);
            chainBindingActions.remove(chatId);

            sendMessage(msg);
        }
    }

    /**
     * Обработчик второй цепной команды
     */
    private void chainBindActionHandler(String chatId, Update update) throws Exception {
        var msg = chainActionStrategy.getStrategy(ChainActionCode.getByCode(chainBindingActions.get(chatId)))
                .callback(update);
        chainBindingActions.remove(chatId);

        sendMessage(msg);
    }

    private void updateDataAndNotify(String chatId, Update update) throws ServiceException {
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        updateDataActionSupport.updateAllProfilesData();

        Map<ClickProfile, List<ProfileAccount>> profileAccountsMap = clickProfileService.getProfileAccountsMap();

        sendMessage(createMessageMarkupClickProfilesInfo(Long.valueOf(chatId),
                "Обновление данных завершено",
                messageId, profileAccountsMap, MarkupCreator.createMarkupActions(Collections.singletonList(MENU_ACTION))));

    }

    private void defaultCommand(Message message) {
        SendMessage send = new SendMessage();
        send.setChatId(message.getChatId());
        send.setText("Я не знаю что ответить на такое...\n" +
                "Ознакомьтесь с командами по команде /help");

        sendMessage(send);
    }

    @Scheduled(cron = TIME_JOB, zone = TIME_ZONE)
    private void updateAllDateByTime() throws ServiceException, InterruptedException {
        List<SendMessage> sendMessages = updateAllProfilesJob.updatedProfilesWithBalanceLess5000();

        for (SendMessage sendMessage : sendMessages) {
            sendMessage(sendMessage);
        }
    }

    private BotApiMethod getExceptionMsg(String chatId, String msg) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(msg);

        return sendMessage;
    }

    private void sendMessage(BotApiMethod message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
    }
}



