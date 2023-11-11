package ru.extreme.bot.clickbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.extreme.bot.clickbot.enums.Role;

import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "CHAT_USER")
public class ChatUser {

    @Id
    private Long chatId;

    private String firstName;

    private String lastName;

    private String username;

    private LocalDate registeredAt;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Override
    public String toString() {
        return "ChatUser{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
