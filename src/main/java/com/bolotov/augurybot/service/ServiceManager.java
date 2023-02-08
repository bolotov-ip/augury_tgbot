package com.bolotov.augurybot.service;

import com.bolotov.augurybot.model.Card;
import com.bolotov.augurybot.model.Task;
import com.bolotov.augurybot.model.User;

import java.util.List;

/**
 * Интерфейс Фасада для бизнес-логики
 * */
public interface ServiceManager {

    public User authentication(User user);

    public List<Task> getWorkingTask();

    public List<Card> getAllCard();

}
