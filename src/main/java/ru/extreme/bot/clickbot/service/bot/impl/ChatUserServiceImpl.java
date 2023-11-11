package ru.extreme.bot.clickbot.service.bot.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.extreme.bot.clickbot.enums.Role;
import ru.extreme.bot.clickbot.exception.ServiceException;
import ru.extreme.bot.clickbot.model.ChatUser;
import ru.extreme.bot.clickbot.repository.ChatUserRepository;
import ru.extreme.bot.clickbot.service.bot.ChatUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервис для работы с пользователем бота
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {

    private final ChatUserRepository chatUserRepository;

    @Override
    public void registerUser(Chat chatUser) {
        Long chatId = chatUser.getId();

        ChatUser newUser = new ChatUser();

        newUser.setChatId(chatId);
        newUser.setRegisteredAt(LocalDate.now());
        newUser.setFirstName(chatUser.getFirstName());
        newUser.setLastName(chatUser.getLastName());
        newUser.setUsername(chatUser.getUserName());
        newUser.setRole(Role.USER);

        chatUserRepository.save(newUser);
    }

    @Override
    public String deleteAdminById(Long id) throws Exception {
        Optional<ChatUser> chatUser = chatUserRepository.findById(id);

        String username;
        if (chatUser.isPresent()) {
            chatUser.get().setRole(Role.USER);
            username = chatUser.get().getFirstName();

            chatUserRepository.save(chatUser.get());
        } else {
            throw new Exception("Выберите удаляемого администратора из списка.");
        }

        return username;
    }

    @Override
    public Role chatUserIsRegisteredAndGetRoel(Long chatUserId) {
        boolean isPresent = chatUserRepository.isPresentChatUser(chatUserId);
        if (isPresent) {
            return chatUserRepository.getChatUserRoleByChatId(chatUserId);
        } else {
            return Role.NONE_REGISTERED;
        }
    }

    @Override
    public List<ChatUser> findAllAdminsName(Long chatId) {
        return chatUserRepository.findAllAdminsName(chatId);
    }

    @Override
    public String setUserAdminRightsById(Long userId) throws ServiceException {
        try {
            ChatUser user = chatUserRepository.getChatUserByChatId(userId);

            if (user == null) {
                throw new Exception();
            }

            user.setRole(Role.ADMIN);
            chatUserRepository.save(user);

            return user.getFirstName();
        } catch (Exception ex) {
            throw new ServiceException("Пользователей с таким ID не найдено, проверьте введенные данные и попробуйте еще раз.", ex);
        }
    }

    @Override
    public List<Long> getChatIdsAllAdmins() {
        return chatUserRepository.getChatIdsAllAdmins();
    }

}
