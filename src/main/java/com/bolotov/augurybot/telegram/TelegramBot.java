package com.bolotov.augurybot.telegram;

import com.bolotov.augurybot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    @Autowired
    BotConfig config;

    @Autowired
    Controller controller;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        ViewAnswer answer = controller.update(update);
        send(answer);
    }

    public void send(ViewAnswer answer) {
        try {
            if(answer.hasMessage())
                execute(answer.getMessage());
            else if(answer.hasDocument())
                execute(answer.getDocument());
            else if (answer.hasMedia()) {
                execute(answer.getMediaGroup());
            }
            else if (answer.hasPhoto()) {
                execute(answer.getSendPhoto());
            }
            else if (answer.hasVideo()) {
                execute(answer.getSendVideo());
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}