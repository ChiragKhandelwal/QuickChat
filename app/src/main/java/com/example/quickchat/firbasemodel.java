package com.example.quickchat;

public class firbasemodel {
    String uid,name,email,image,status;

    public firbasemodel() {
    }

    public firbasemodel(String uid, String name, String email, String image,String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.image = image;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
