package com.android.paskahlis.anchat.entity;

import android.view.View;

/**
 * Created by paskahlis on 22/02/2018.
 */

public class EntityChat {
    public static final int MESSAGE_DIRECTION_IN = 2;
    public static final int MESSAGE_DIRECTION_OUT = 3;

    public static final String NONE = "none";
    public static final String ROOT = "chats";

    private String mesageSender;
    private int messageDirection;
    private EntityChatContent content;

    public EntityChat() {
        /*firebase intention*/
    }

    public EntityChat(String mesageSender, int messageDirection, EntityChatContent content) {
        this.mesageSender = mesageSender;
        this.messageDirection = messageDirection;
        this.content = content;
    }

    public String getMesageSender() {
        return mesageSender;
    }

    public int getMessageDirection() {
        return messageDirection;
    }

    public EntityChatContent getContent() {
        return content;
    }

    public void setMesageSender(String mesageSender) {
        this.mesageSender = mesageSender;
    }

    public void setMessageDirection(int messageDirection) {
        this.messageDirection = messageDirection;
    }

    public void setContent(EntityChatContent content) {
        this.content = content;
    }
}
