package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChatContent {
    public static final int MESSAGE_CONTENT_TYPE_TEXT = 0;
    public static final int MESSAGE_CONTENT_TYPE_FILE = 1;

    protected int messageType;
    protected Object message;

    public EntityChatContent() {
    }

    public EntityChatContent(int mt) {
        messageType = mt;
    }

    public int getMessageType() {
        return messageType;
    }

    public Object getMessage() {
        return message;
    }
}
