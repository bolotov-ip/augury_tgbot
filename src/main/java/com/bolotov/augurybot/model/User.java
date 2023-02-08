package com.bolotov.augurybot.model;

import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="role", discriminatorType = DiscriminatorType.STRING)
@Table(name="users")
public class User {
    @Id
    Long chatId;

    @Column
    LocalDateTime lastVisitTime;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(LocalDateTime lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chatId.equals(user.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    public static User valueOf(Update update) {
        User user = new User();
        return user;
    }
}
