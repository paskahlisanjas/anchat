package com.android.paskahlis.anchat.model;

/**
 * Created by fajar on 22/02/18.
 */

public class ChatPreview {
    private String profilePic;
    private String name;
    private String textChat;
    private String timestamp;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
