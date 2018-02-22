package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChatContentFile extends EntityChatContent {
    private final int MESSAGE_CONTENT_TYPE = MESSAGE_CONTENT_TYPE_TEXT;

    private View messageView;

    @Override
    public int getMessageContentType() {
        return MESSAGE_CONTENT_TYPE;
    }

    public View getMessageView() {
        return messageView;
    }

    public void setMessageView(View view) {
        messageView = view;
    }
}
