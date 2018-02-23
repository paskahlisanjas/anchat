package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChatContentFile extends EntityChatContent {
    private final static int MESSAGE_CONTENT_TYPE = MESSAGE_CONTENT_TYPE_TEXT;

    private String fileName;

    public EntityChatContentFile() {

    }

    public EntityChatContentFile(String fileName) {
        super(MESSAGE_CONTENT_TYPE);
        this.fileName = fileName;
    }
}
