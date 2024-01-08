package net.salesianos.server.threads;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import net.salesianos.shared.models.Message;

public class ClientHandler extends Thread {
    
    private Socket clientSocket;

    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }

    @Override 
    public void run() {
        String username = "";
        try {
            ObjectInputStream objInStream = new ObjectInputStream(this.clientSocket.getInputStream());

            username = objInStream.readUTF();

            while (true) {
                Message msg = (Message) objInStream.readObject();
                System.out.println(username + ": " + msg.getContent());
            }

        } catch (EOFException eofException) {
            System.out.println("CERRANDO CONEXION CON " + username.toUpperCase());
            // TODO: handle exception
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
