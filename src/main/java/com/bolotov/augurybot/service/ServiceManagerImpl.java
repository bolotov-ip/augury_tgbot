package com.bolotov.augurybot.service;

import com.bolotov.augurybot.model.Card;
import com.bolotov.augurybot.model.Task;
import com.bolotov.augurybot.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceManagerImpl implements ServiceManager{
    @Override
    public User authentication(User user) {
        return null;
    }

    @Override
    public List<Task> getWorkingTask() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setId(1L);
        task.setNameProduct("Здоровье");
        tasks.add(task);
        task = new Task();
        task.setId(2L);
        task.setNameProduct("Нумерология");
        tasks.add(task);
        task = new Task();
        task.setId(3L);
        task.setNameProduct("Индивидуальная карта");
        tasks.add(task);
        return tasks;
    }

    @Override
    public List<Card> getAllCard() {
        List<Card> cards = new ArrayList<>();
        for(int i = 1; i<79; i++) {
            Card card = new Card();
            card.setId(1L);
            card.setNameCard("Карта " + i);
            cards.add(card);
        }
        return cards;
    }
}
