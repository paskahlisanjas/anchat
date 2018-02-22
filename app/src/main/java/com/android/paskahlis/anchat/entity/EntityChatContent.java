package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChatContent {
    public static final int MESSAGE_CONTENT_TYPE_TEXT = 0;
    public static final int MESSAGE_CONTENT_TYPE_FILE = 1;

    public int getMessageContentType() {
        return -1;
    }

    public View getMessageView() {
        return null;
    }
}
