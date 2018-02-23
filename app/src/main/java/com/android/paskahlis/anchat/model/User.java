package com.android.paskahlis.anchat.model;

/**
 * Created by fajar on 23/02/18.
 */

public class User {
    private String name;
    private String email;
    private String status;
    private String profPic;

    public User(String name, String email, String status, String profPic) {
        this.name = name;
        this.email = email;
        this.status = status;
        this.profPic = profPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfPic() {
        return profPic;
    }

    public void setProfPic(String profPic) {
        this.profPic = profPic;
    }
}
