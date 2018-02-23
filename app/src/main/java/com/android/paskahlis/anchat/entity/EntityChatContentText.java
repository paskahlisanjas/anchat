package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChatContentText extends EntityChatContent {
    private final static int MESSAGE_CONTENT_TYPE = MESSAGE_CONTENT_TYPE_TEXT;

    private String message;

    public EntityChatContentText() {}
    public EntityChatContentText(String message) {
        super(MESSAGE_CONTENT_TYPE);
        this.message = message;
    }

    @Override
    public int getMessageType() {
        return MESSAGE_CONTENT_TYPE;
    }

    @Override
    public Object getMessage() {
        return message;
    }
}
