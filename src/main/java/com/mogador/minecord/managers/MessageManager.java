package com.mogador.minecord.managers;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

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

    private static final int QUEUE_SIZE = 10;

    Queue<String> lastMessages;

    public void initialize() {
        lastMessages = EvictingQueue.create(QUEUE_SIZE);
    }

    public void add(String msg) {
        lastMessages.add(msg);
    }

    public List<String> getLastMessages(int nb) {
        if(nb > QUEUE_SIZE) {
            throw new IllegalArgumentException(String.format("You can't get more than %d messages", QUEUE_SIZE));
        }

        int offset = lastMessages.size() - nb;
        if(offset < 0) offset = 0;

        return lastMessages.stream()
            .skip(offset)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
