package com.example.counselor_side_app.models;

public class Chat {


    private String sender;
    private String receiver;
    private String message;
    private String time_sent;
    private boolean isseen;


    public Chat(String sender) {
        this.sender = sender;
    }

    public Chat(String sender, String receiver, String message, boolean isseen,String time_sent) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.time_sent = time_sent;
    }
    public Chat(){

    }

    public String getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(String time_sent) {
        this.time_sent = time_sent;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

