package net.salesianos.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import net.salesianos.client.threads.ServerListener;
import net.salesianos.shared.models.Message;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        int userOption = 0;
        final Scanner SCANNER = new Scanner(System.in);
        
        System.out.println("¿Cómo te llamas?");
        String username = SCANNER.nextLine();

        Socket socket = new Socket("localhost", 50000);
        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
        //ServerListener serverListener = new ServerListener(objInStream);
        //serverListener.start();
        while (userOption != -1) {
            System.out.println("Va a enviar datos de persona al servidor.");
            Message msg = new Message();
            msg.setUsername(username);
            System.out.print("Introduzca el mensaje: ");
            String content = SCANNER.nextLine();
            if (content.substring(0, 3).equals("msg:")) {
                msg.setContent(content.substring(4, content.length()));
            } else if (content.substring(0, 3).equals("bye")) {
                userOption = -1;
            }
            SCANNER.nextLine();
            objOutStream.writeObject(msg);
            try {
                System.out.println("Pulse -1 pa salir o cualquier cosa para continuar: ");
                userOption = SCANNER.nextInt();
                SCANNER.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Continuamos...");
            }
        }
        SCANNER.close();
        objOutStream.close();
        socket.close();
    }
}
