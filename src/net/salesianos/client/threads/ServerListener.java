package net.salesianos.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.salesianos.shared.models.Message;

public class ServerListener extends Thread{
    private ObjectInputStream objInStream;
    public ServerListener(ObjectInputStream socketObjectInputStream) {
        this.objInStream = socketObjectInputStream;
    }

    //hacer aqui lo de recibir mensajes de servidor
    // ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream, 
    @Override
    public void run() {
        try {
            while (true) {
                Message newMessage = (Message) this.objInStream.readObject();
                if (newMessage.getUsername().equals("ServerOut")) {
                    System.out.println(newMessage.getContent());
                } else {
                    System.out.println(newMessage.getUsername() + ": " + newMessage.getContent());
                }
            }
        } catch (ClassNotFoundException e1) {
            System.out.println("No se encontró la clase message");
        } catch (IOException e2) {
            System.out.println("Saliendo del chat...");
        }
    }
}
