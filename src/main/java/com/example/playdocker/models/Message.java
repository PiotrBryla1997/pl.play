package com.example.playdocker.models;

public class Message {
    private String id;
    private String receiverNumber;//Numer odbiorcy
    private String senderNumber;//Numer nadawcy
    private String content;//Zawartość wiadomości

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message() {
    }

    public Message(String id, String receiverNumber, String senderNumber, String content) {
        this.id = id;
        this.receiverNumber = receiverNumber;
        this.senderNumber = senderNumber;
        this.content = content;
    }
}
