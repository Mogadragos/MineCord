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

    private int queueSize;

    Queue<String> lastMessages;

    public void initialize(int queueSize) {
        this.queueSize = queueSize;
        lastMessages = EvictingQueue.create(queueSize);
    }

    public void add(String msg) {
        lastMessages.add(msg);
    }

    public List<String> getLastMessages(int nb) {
        if(nb > queueSize) {
            throw new IllegalArgumentException(String.format("You can't get more than %d messages", queueSize));
        }

        int offset = lastMessages.size() - nb;
        if(offset < 0) offset = 0;

        return lastMessages.stream()
            .skip(offset)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
