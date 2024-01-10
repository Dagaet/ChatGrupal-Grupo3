package net.salesianos.server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.salesianos.server.threads.ClientHandler;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(55000);

        while (true) {
            System.out.println("Esperando conexión...");
            Socket clienSocket = serverSocket.accept();
            System.out.println("CONEXIÓN ESTABLECIDA");
            ClientHandler clientHandler = new ClientHandler(clienSocket);
            clientHandler.start();

        }
    }
}
