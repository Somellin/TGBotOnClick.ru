package ru.extreme.bot.clickbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extreme.bot.clickbot.enums.Role;
import ru.extreme.bot.clickbot.model.ChatUser;

import java.util.List;

/**
 * Репозиторий для пользователей чат-бота
 */
@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    /**
     * Проверка на существование пользователя
     */
    @Query("select case when (count(cu) > 0)  then true else false end from CHAT_USER cu where cu.chatId = ?1")
    Boolean isPresentChatUser(Long chatId);

    /**
     * Получить роль текущего пользователя
     */
    @Query("select cu.role from CHAT_USER cu where cu.chatId = ?1")
    Role getChatUserRoleByChatId(Long chatId);

    /**
     * Получить всех пользователей с ролью ADMIN
     */
    @Query("select cu from CHAT_USER cu where cu.role = 'ADMIN' and cu.chatId != ?1")
    List<ChatUser> findAllAdminsName(Long chatId);

    /**
     * Получение пользователя по id
     */
    ChatUser getChatUserByChatId(Long chatId);

    /**
     * Список id админов бота
     */
    @Query("select cu.chatId from CHAT_USER cu where cu.role = 'ADMIN'")
    List<Long> getChatIdsAllAdmins();
}
