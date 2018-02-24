package com.android.paskahlis.anchat.entity;

/**
 * Created by paskahlis on 25/02/2018.
 */

public class FirebaseEntityChat {
    public static final int CHAT_TYPE_TEXT = 0;
    public static final int CHAT_TYPE_FILE = 1;

    private String content;
    private String text;

    public FirebaseEntityChat() {
        /*firebase needs*/
    }

    public FirebaseEntityChat(String c, String t) {
        content = c;
        text = t;
    }

    public String getContent() {
        return content;
    }

    public String getText() {
        return text;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setText(String text) {
        this.text = text;
    }
}
