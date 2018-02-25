package com.android.paskahlis.anchat.entity;

/**
 * Created by paskahlis on 25/02/2018.
 */

public class FirebaseEntityChat {
    public static final int CHAT_TYPE_TEXT = 0;
    public static final int CHAT_TYPE_FILE = 1;
    public static final String CONTENT_NONE = "-";
    public static final String CHAT_ROOT = "chats";

    private String content;
    private String text;
    private String sender;

    public FirebaseEntityChat() {
        /*firebase needs*/
    }

    public FirebaseEntityChat(String c, String t, String s) {
        content = c;
        text = t;
        sender = s;
    }

    public String getContent() {
        return content;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
