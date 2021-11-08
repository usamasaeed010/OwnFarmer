package com.farmers.ownfarmer.ui.chat;

public class UserMessage {
    String string;

    public UserMessage(String string) {
        this.string = string;
    }

    public UserMessage() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
