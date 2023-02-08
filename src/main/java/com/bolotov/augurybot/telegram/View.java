package com.bolotov.augurybot.telegram;

import com.bolotov.augurybot.model.*;
import com.bolotov.augurybot.service.ServiceListener;
import com.bolotov.augurybot.service.ServiceManager;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class View implements ServiceListener {

    @Autowired
    ServiceManager serviceManager;

    private Map<User, ViewState> state = new WeakHashMap<>();

    private List<Task> tasks;

    private List<Card> cards;

    @Override
    public void event(ServiceEvent e) {

    }

    public ViewAnswer listCard(Update update, int numberPage) {
        if(cards == null)
            cards = serviceManager.getAllCard();
        int pageSize = 8;
        List<Button> buttons = new ArrayList<>();
        int countPage = fillButtonPage(buttons, cards, ButtonCallback.BTN_CARD.toString(), pageSize, numberPage);
        buttons.add(new Button(ButtonCallback.BTN_BACK));
        ViewAnswer answer = getAnswer(update,  buttons, 1);
        answer.setText("Список карт");
        addNavigateKeyboard(answer, ButtonCallback.BTN_ANSWER_CARD.toString(), numberPage, countPage);
        return answer;
    }

    public ViewAnswer task(Update update, int numberPage, long idTask) {
        Task task = getItem(tasks, idTask);
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(ButtonCallback.BTN_ANSWER_TEXT));
        buttons.add(new Button(ButtonCallback.BTN_ANSWER_IMAGE));
        buttons.add(new Button(ButtonCallback.BTN_ANSWER_CARD));
        buttons.add(new Button(ButtonCallback.BTN_BACK));
        int countColumnButton = 1;
        ViewAnswer answer = getAnswer(update, buttons, countColumnButton);
        String textTask =
                String.format("Задача № %d\nЗаказчик: %s\nТип: %s\nКомментарий: %s\n",
                task.getId(), task.getAuthor(), task.getOrderType(), task.getComment());
        answer.setText(textTask);
        return answer;
    }

    public ViewAnswer listTasks(Update update, int numberPage) {
        if(tasks==null)
            tasks = serviceManager.getWorkingTask();
        int pageSize = 1;
        List<Button> buttons = new ArrayList<>();
        int countPage = fillButtonPage(buttons, tasks, ButtonCallback.BTN_TASK.toString(), pageSize, numberPage);
        buttons.add(new Button(ButtonCallback.BTN_BACK));
        ViewAnswer answer = getAnswer(update,  buttons, 1);
        answer.setText("Список задач:");
        addNavigateKeyboard(answer, ButtonCallback.BTN_TASKS.toString(), numberPage, countPage);
        return answer;
    }

    public ViewAnswer mainMenu(UserBasic user, Update update) {
        return null;
    }

    public ViewAnswer mainMenu(UserAdmin user, Update update) {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button(ButtonCallback.BTN_PROPERTIES));
        buttons.add(new Button(ButtonCallback.BTN_TASKS));
        buttons.add(new Button(ButtonCallback.BTN_NEXT_TASK));
        ViewAnswer viewAnswer = getAnswer(update, buttons, 1);
        viewAnswer.setText("Добро пожаловать!");
        return viewAnswer;
    }

    private int fillButtonPage(List<Button> buttons, List<? extends ViewButton> items, String prefix, int pageSize, int numberPage) {
        int countPage = 1;
        if(items.size()>0) {
            countPage = (int) Math.ceil(items.size()/(double)pageSize);
            int startPosition = (pageSize *(numberPage-1));
            int endPosition = Math.min(startPosition + pageSize, items.size());
            for(int i = startPosition; i<endPosition; i++) {
                Button btn = new Button(items.get(i).getName());
                btn.setCallbackData(prefix + "__" + items.get(i).getId() + "__" + numberPage);
                buttons.add(btn);
            }
        }
        return countPage;
    }

    private ViewAnswer addNavigateKeyboard(ViewAnswer answerBot, String callback, int numberPage, int countPage) {

        ReplyKeyboard replyKeyboard = answerBot.getMessage() instanceof SendMessage ?
                ((SendMessage) answerBot.getMessage()).getReplyMarkup() :
                ((EditMessageText) answerBot.getMessage()).getReplyMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = ((InlineKeyboardMarkup) replyKeyboard).getKeyboard();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        if(numberPage>1) {
            InlineKeyboardButton buttonPrev = new InlineKeyboardButton();
            buttonPrev.setText("<");
            buttonPrev.setCallbackData(callback + "__" + (numberPage - 1));
            rowInLine.add(buttonPrev);
        }
        if(numberPage>1 || numberPage<countPage) {
            InlineKeyboardButton btnInfo = new InlineKeyboardButton();
            btnInfo.setText(numberPage + " - " + countPage);
            btnInfo.setCallbackData(callback + "__" + numberPage);
            rowInLine.add(btnInfo);
        }
        if(numberPage<countPage) {
            InlineKeyboardButton buttonNext = new InlineKeyboardButton();
            buttonNext.setText(">");
            buttonNext.setCallbackData(callback + "__" + (numberPage + 1));
            rowInLine.add(buttonNext);
        }
        rowsInLine.add(rowInLine);
        ((InlineKeyboardMarkup) replyKeyboard).setKeyboard(rowsInLine);
        replyMarkup((InlineKeyboardMarkup) replyKeyboard, answerBot.getMessage());
        return answerBot;
    }

    public ViewAnswer commandNotSupport(User user) {
        SendMessage send = new SendMessage(String.valueOf(user.getChatId()), "");
        ViewAnswer answer = new ViewAnswer(send);
        answer.setText("Команда не поддерживается");
        return answer;
    }

    private ViewAnswer getAnswer(Update update, List<Button> btnList, int countColumn) {
        if(update.hasCallbackQuery())
            return setCommonCallbackAnswer(update, btnList, countColumn);
        else
            return setCommonAnswer(update, btnList, countColumn);
    }

    private ViewAnswer setCommonCallbackAnswer(Update update, List<Button> btnList, int countColumn) {
        Message msg = update.getCallbackQuery().getMessage();
        String chatId = String.valueOf(msg.getChatId());
        long messageId = msg.getMessageId();
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId((int)messageId);
        setButton(btnList, editMessage, countColumn);
        ViewAnswer answer = new ViewAnswer(editMessage);
        return answer;
    }

    private ViewAnswer setCommonAnswer(Update update, List<Button> btnList, int countColumn) {
        ViewAnswer answer = new ViewAnswer();
        Message message = update.getMessage();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage(chatId, "");
        setButton(btnList, sendMessage, countColumn);
        answer.setMessage(sendMessage);
        return answer;
    }

    private void replyMarkup(InlineKeyboardMarkup markupInLine, BotApiMethod<?> msg) {
        if(msg instanceof SendMessage)
            ((SendMessage)msg).setReplyMarkup(markupInLine);
        else if(msg instanceof EditMessageText) {
            ((EditMessageText)msg).setReplyMarkup(markupInLine);
        }
    }

    private void setButton(List<Button> btnList, BotApiMethod<?> msg, int countColumn) {
        if(btnList==null || btnList.size()==0)
            return;
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        int countAddRowBtn = 0;
        for(Button btn : btnList) {
            if(countAddRowBtn<countColumn){
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(btn.getText());
                button.setCallbackData(btn.getCallbackData());
                rowInLine.add(button);
                countAddRowBtn++;
            }
            else {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(btn.getText());
                button.setCallbackData(btn.getCallbackData());
                rowInLine.add(button);
                countAddRowBtn=1;
            }
        }
        if(!rowsInLine.contains(rowInLine))
            rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);
        replyMarkup(markupInLine, msg);
    }

    private <T extends ViewButton>T getItem(List<T> items, long id) {
        for(T item : items) {
            if(item.getId() == id)
                return item;
        }
        return null;
    }
}
