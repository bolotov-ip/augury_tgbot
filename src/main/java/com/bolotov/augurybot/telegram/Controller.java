package com.bolotov.augurybot.telegram;

import com.bolotov.augurybot.model.Task;
import com.bolotov.augurybot.model.User;
import com.bolotov.augurybot.model.UserAdmin;
import com.bolotov.augurybot.model.UserBasic;
import com.bolotov.augurybot.service.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

    @Autowired
    View view;

    public ViewAnswer update(Update update) {
        //User user = serviceManager.authentication(User.valueOf(update));
        User user = new UserAdmin();
        Long chatId = update.hasCallbackQuery()? update.getCallbackQuery().getMessage().getChatId() : update.getMessage().getChatId();
        user.setChatId(chatId);
        if(user instanceof UserAdmin)
            return handlerAdmin((UserAdmin) user, update);
        if(user instanceof UserBasic)
            return handlerUser((UserBasic) user, update);
        return null;
    }

    public ViewAnswer handlerAdmin(UserAdmin user, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            switch (command) {
                case "/start":
                    return view.mainMenu(user, update);
            }
        }
        else if(update.hasCallbackQuery()) {
            String[] callbackData = update.getCallbackQuery().getData().split("__");
            ButtonCallback callbackCommand = getCommand(callbackData);
            int number = getNumberPage(callbackData);
            long id = getIdPage(callbackData);
            if(callbackCommand!=null) {
                switch (callbackCommand) {
                    case BTN_TASKS:
                        return view.listTasks(update, number);
                    case BTN_TASK:
                        return view.task(update, number, id);
                    case BTN_ANSWER_CARD:
                        return view.listCard(update, number);
                    default:
                        return view.commandNotSupport(user);
                }
            }

        }
        return new ViewAnswer();
    }

    public ViewAnswer handlerUser(UserBasic user, Update update) {
        return new ViewAnswer();
    }

    public ButtonCallback getCommand(String[] callbackData) {
        ButtonCallback callbackCommand = ButtonCallback.convert(callbackData[0]);
        return callbackCommand;
    }

    public int getNumberPage(String[] callbackData) {
        int number = 1;
        if(callbackData.length>1) {
            number = Integer.valueOf(callbackData[callbackData.length-1]);
        }
        return number;
    }

    public long getIdPage(String[] callbackData) {
        long id = 0L;
        if(callbackData.length > 2) {
            id = Long.valueOf(callbackData[1]);
        }
        return id;
    }
}
