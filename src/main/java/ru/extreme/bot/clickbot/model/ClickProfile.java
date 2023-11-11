package ru.extreme.bot.clickbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "CLICK_PROFILE")
public class ClickProfile {

    @Id
    private Long id;

    private String login;

    private Timestamp createdAt;

    private String description;

    private String firstName;

    private String lastName;

    private String middleName;

    private Double balance;

    private String email;

    private String encodeToken;

    @Override
    public String toString() {
        return "ClickProfile{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", createdAt=" + createdAt +
                ", description='" + description + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", balance=" + balance +
                ", email='" + email + '\'' +
                ", encodeToken='" + encodeToken + '\'' +
                '}';
    }
}
