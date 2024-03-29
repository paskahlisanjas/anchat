package com.android.paskahlis.anchat.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paskahlis on 23/02/2018.
 */

public class EntityUser {
    public static final String USER_ROOT = "users";
    public static final String USER_MSG_TOKEN = "msgToken";

    private String displayName = "";
    private List<String> contactList = new ArrayList<>();
    private Double latitude = 0d;
    private Double longitude = 0d;
    private String profilePictureUrl = "";
    private String status = "";
    private String msgToken = "";

    public EntityUser() {
        /*Firebase intention*/
    }

    public EntityUser(String displayName, List<String> contactList, Double latitude, Double longitude, String profilePicture,String status) {
        this.displayName = displayName;
        this.contactList = contactList;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profilePictureUrl = profilePicture;
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getContactList() {
        return contactList;
    }

    public String getProfilePicture() {
        return profilePictureUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePictureUrl = profilePicture;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addContact(String contact) {
        contactList.add(contact);
    }

    public void setMsgToken(String token) {
        msgToken = token;
    }

    public String getMsgToken() {
        return msgToken;
    }
}
