package com.android.paskahlis.anchat.model;

import java.util.Date;
import java.util.Timer;

/**
 * Created by fajar on 22/02/18.
 */

public class ChatPreview {
    private String profilePic;
    private String name;
    private String textChat;
    private String timestamp;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextChat() {
        return textChat;
    }

    public void setTextChat(String textChat) {
        this.textChat = textChat;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
