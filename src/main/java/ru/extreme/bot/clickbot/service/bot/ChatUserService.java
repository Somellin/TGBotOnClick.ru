package ru.extreme.bot.clickbot.service.bot;

import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.extreme.bot.clickbot.enums.Role;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.model.ChatUser;

import java.util.List;

/**
 * Сервис для работы с пользователем бота
 */
public interface ChatUserService {

    /**
     * Регистрация нового пользователя
     */
    void registerUser(Chat chatUser);

    /**
     * Удаление прав администратора
     */
    String deleteAdminById(Long id) throws Exception;

    /**
     * Получение роли пользователя
     */
    Role chatUserIsRegisteredAndGetRoel(Long chatUserId);

    /**
     * Получение пользователей с правами администратора
     */
    List<ChatUser> findAllAdminsName(Long chatId);

    /**
     * Выдача прав администратора пользователю
     */
    String setUserAdminRightsById(Long userId) throws ServiceException;

    /**
     * Список id чатов админов
     */
    List<Long> getChatIdsAllAdmins();
}
