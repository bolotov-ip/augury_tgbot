package com.bolotov.augurybot.telegram;

import java.util.HashMap;
import java.util.Map;

enum ViewState {
    VIEW_PRODUCT;


    private static Map<String, String> textMessage = new HashMap<String, String>();
    static {
        textMessage.put("ADMIN_START", "Добро пожаловать.\nВы являетесь администратором.");
    }

    public String getTextMessage() {
        return textMessage.get(this.toString());
    }
}
