package com.mogador.minecord.managers;

import java.util.Arrays;
import java.util.Queue;

import com.google.common.collect.EvictingQueue;

public class MessageManager {
    private static MessageManager instance;
    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }
    private MessageManager() {}

    static final int QUEUE_SIZE = 10;
    static final String[] STRING_ARRAY = new String[QUEUE_SIZE];


    Queue<String> lastMessages;
    
    public void initialize() {
        lastMessages = EvictingQueue.create(QUEUE_SIZE);
    }

    public void add(String msg) {
        lastMessages.add(msg);
    }

    public String[] getLastMessages() {
        return getLastMessages(QUEUE_SIZE);
    }

    public String[] getLastMessages(int nb) {
        String[] messages = lastMessages.toArray(STRING_ARRAY);
        if(nb >= messages.length) return messages;
        return Arrays.copyOfRange(messages, 0, nb);
    }
}
