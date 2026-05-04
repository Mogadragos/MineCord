package com.mogador.minecord.managers;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.google.common.collect.EvictingQueue;
import com.mogador.minecord.data.MessageData;

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

    Queue<MessageData> lastMessages;

    public void initialize(int queueSize) {
        this.queueSize = queueSize;
        lastMessages = EvictingQueue.create(queueSize);
    }

    public void add(Player player, String msg) {
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String formattedDate = formatter.format(instant);

        
        lastMessages.add(new MessageData(player, msg, formattedDate));
    }

    public List<MessageData> getLastMessages(int nb) {
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
