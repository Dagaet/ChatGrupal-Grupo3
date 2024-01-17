package net.salesianos.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import net.salesianos.client.threads.ServerListener;
import net.salesianos.shared.models.Message;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        final Scanner SCANNER = new Scanner(System.in);
        System.out.println("¿Cómo te llamas?");
        String username = SCANNER.nextLine();
        System.out.println("Hola " + username);
        System.out.println("Ultimos mensajes: ");
        Socket socket = new Socket("localhost", 55000);

        ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
        ServerListener serverListener = new ServerListener(objInStream);
        serverListener.start();

        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
        objOutStream.writeUTF(username);
        objOutStream.flush();

        Message msg = new Message();

        while (!msg.getContent().equals("bye")) {
            msg = new Message("", "", "");
            msg.setUsername(username);
            String content = SCANNER.nextLine();
            msg.setContent(content);
            objOutStream.writeObject(msg);
        }
        SCANNER.close();
        objOutStream.close();
        socket.close();
    }
}
