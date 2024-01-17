package net.salesianos.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.salesianos.shared.models.Message;

public class ServerListener extends Thread {
    private ObjectInputStream objInStream;

    public ServerListener(ObjectInputStream socketObjectInputStream) {
        this.objInStream = socketObjectInputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message newMessage = (Message) this.objInStream.readObject();
                System.out.println(newMessage);
            }
        } catch (IOException | ClassNotFoundException e2) {
            System.out.println("Saliendo del chat...");
        }
    }
}
