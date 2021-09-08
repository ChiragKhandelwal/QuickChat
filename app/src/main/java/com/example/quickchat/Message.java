package com.example.quickchat;

public class Message {
    String message,senderID;
    long timeStrap;

    public Message(String message, String senderID, long timeStrap) {
        this.message = message;
        this.senderID = senderID;
        this.timeStrap = timeStrap;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimeStrap() {
        return timeStrap;
    }

    public void setTimeStrap(long timeStrap) {
        this.timeStrap = timeStrap;
    }
}
