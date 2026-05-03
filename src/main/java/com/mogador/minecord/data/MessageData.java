package com.mogador.minecord.data;

public class MessageData {
    String author;
    String message;
    String timestamp;
    String format;
    
    public MessageData(String author, String message, String timestamp) {
        this.author = author;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
