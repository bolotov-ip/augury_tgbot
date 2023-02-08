package com.bolotov.augurybot.telegram;

class Button {
    String stringButton;
    ButtonCallback buttonCallback;
    String callbackData;

    public Button(String name) {
        stringButton = name;
    }

    public Button(ButtonCallback name) {
        buttonCallback = name;
    }

    public String getCallbackData() {
        if(callbackData!=null)
            return callbackData;
        else if(buttonCallback !=null)
            return buttonCallback.toString();
        else
            return stringButton;
    }

    public String getText() {
        if(buttonCallback !=null)
            return buttonCallback.getText();
        else
            return stringButton;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
