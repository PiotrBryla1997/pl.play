package com.example.playdocker.models;

public class Message {
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

    public Message() {
    }

    public Message(String receiverNumber, String senderNumber, String content) {
        this.receiverNumber = receiverNumber;
        this.senderNumber = senderNumber;
        this.content = content;
    }
}
