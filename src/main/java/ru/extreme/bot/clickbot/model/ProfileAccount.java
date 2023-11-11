package ru.extreme.bot.clickbot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "PROFILE_ACCOUNT")
public class ProfileAccount {
    @Id
    private Long id;

    private String name;

    private String service;

    private int serviceId;

    private String serviceLogin;

    private String status;

    private String state;

    private Timestamp createdAt;

    private Double balance;

    private Long profileId;

    @Override
    public String toString() {
        return "ProfileAccount{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", service='" + service + '\'' +
                ", serviceId=" + serviceId +
                ", serviceLogin='" + serviceLogin + '\'' +
                ", status='" + status + '\'' +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                ", balance=" + balance +
                ", profileId=" + profileId +
                '}';
    }
}
