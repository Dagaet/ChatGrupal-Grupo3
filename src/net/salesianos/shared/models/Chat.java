package net.salesianos.shared.models;

import java.util.ArrayList;

public class Chat {
    private ArrayList<Message> chatLog = new ArrayList<>();

    public Chat() {
    }

    public ArrayList<Message> getAllMessages() {
        return this.chatLog;
    }

    public synchronized void addMessage(Message message) {
        chatLog.add(message);
    }
}
