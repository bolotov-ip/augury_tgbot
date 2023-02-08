package com.bolotov.augurybot.telegram;

public enum ButtonCallback {
    BTN_SEPARATOR("--------------------------------------"),
    BTN_BACK("Назад"),
    BTN_TASKS("Задачи"),
    BTN_TASK("Задача"),
    BTN_NEXT_TASK("Следующая задача"),
    BTN_PROPERTIES("Настройки"),
    BTN_CANCEL("Отмена"),
    BTN_ANSWER_IMAGE("Дать ответ картинкой"),
    BTN_ANSWER_TEXT("Дать ответ текстом"),
    BTN_ANSWER_CARD("Дать ответ картой"),
    BTN_CARD("Карта");

    private String text;

    ButtonCallback(String txt) {
        text = txt;
    }

    public String getText() {
        return text;
    }

    public static ButtonCallback convert(String command) {
        try{
            ButtonCallback button = ButtonCallback.valueOf(command);
            return button;
        }
        catch (Exception e) {
            return null;
        }
    }
}
